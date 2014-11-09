package org.scrutmydocs.jobs;

import com.google.common.base.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.scrutmydocs.ScrutMyDocsTests;
import org.scrutmydocs.plugins.fs.FileSystemConverter;
import org.scrutmydocs.services.SMDDocumentService;
import org.scrutmydocs.services.SMDRepositoriesService;
import org.scrutmydocs.domain.SMDSearchQuery;
import org.scrutmydocs.domain.SMDSearchResponse;
import org.scrutmydocs.exceptions.SMDJsonParsingException;
import org.scrutmydocs.plugins.fs.FSSMDRepository;
import org.scrutmydocs.plugins.fs.FSSMDRepositoryScan;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

public class TestScan extends ScrutMyDocsTests {
	File dir;

    @Inject
    public TestScan(SMDRepositoriesService repositoriesService,
                    SMDDocumentService searchService,
                    FileSystemConverter fileSystemConverter) {
        super(repositoriesService, searchService, fileSystemConverter);
    }

    @Before
	public void createTmpDirectory() throws IOException {
		dir = Files.createTempDirectory("scrutmydocs").toFile();
	}

	@Test
	public void testScanAdd() throws InterruptedException, IOException, URISyntaxException, SMDJsonParsingException {
		FSSMDRepository fssmdPlugin = new FSSMDRepository(dir.getAbsolutePath());
		new FSSMDRepositoryScan(searchService, fileSystemConverter).scrut(fssmdPlugin);

        // We wait 6 seconds
        // TODO We should have something in elasticsearch saying that
        // we have run a first time
        awaitBusy(new Predicate<Object>() {
            @Override
            public boolean apply(Object input) {
                return false;
            }
        }, 6, TimeUnit.SECONDS);

		SMDSearchResponse searchResponse = searchService.search(new SMDSearchQuery("*", 0, 1, null));
        assertThat(searchResponse.totalHits, is(0L));

		// add file
		File tmp = File.createTempFile("add-file", "tmp", dir);
		
		new FSSMDRepositoryScan(searchService, fileSystemConverter).scrut(fssmdPlugin);

		Thread.sleep(6 * 1000);

        assertThat("We should have a new document indexed", awaitBusy(new Predicate<Object>() {
            @Override
            public boolean apply(Object input) {
                SMDSearchResponse searchResponse = null;
                try {
                    searchResponse = searchService.search(new SMDSearchQuery("*", 0, 1, null));
                } catch (SMDJsonParsingException e) {
                    fail(e.getMessage());
                }
                return searchResponse.totalHits == 1;
            }
        }, 6, TimeUnit.SECONDS), is(true));

		// remove file
		tmp.delete();

		new FSSMDRepositoryScan(searchService, fileSystemConverter).scrut(fssmdPlugin);

        assertThat("We should have 0 document indexed", awaitBusy(new Predicate<Object>() {
            @Override
            public boolean apply(Object input) {
                SMDSearchResponse searchResponse = null;
                try {
                    searchResponse = searchService.search(new SMDSearchQuery("*", 0, 1, null));
                } catch (SMDJsonParsingException e) {
                    fail(e.getMessage());
                }
                return searchResponse.totalHits == 0;
            }
        }, 6, TimeUnit.SECONDS), is(true));
	}
}
