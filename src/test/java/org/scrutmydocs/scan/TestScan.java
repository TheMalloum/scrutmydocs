package org.scrutmydocs.scan;

import com.google.common.base.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.scrutmydocs.ScrutMyDocsTests;
import org.scrutmydocs.contract.SMDSearchQuery;
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.repositories.fs.FSSMDRepositoryData;
import org.scrutmydocs.repositories.fs.FSSMDRepositoryScan;
import org.scrutmydocs.search.SMDSearchFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestScan extends ScrutMyDocsTests {
	File dir;

	@Before
	public void createTmpDirectory() throws IOException {
		dir = Files.createTempDirectory("scrutmydocs").toFile();
	}

	@Test
	public void testScanAdd() throws InterruptedException, IOException, URISyntaxException {
		FSSMDRepositoryData fssmdPlugin = new FSSMDRepositoryData(dir.getAbsolutePath());
		new FSSMDRepositoryScan().scrut(fssmdPlugin);

        // We wait 6 seconds
        // TODO We should have something in elasticsearch saying that
        // we have run a first time
        awaitBusy(new Predicate<Object>() {
            @Override
            public boolean apply(Object input) {
                return false;
            }
        }, 6, TimeUnit.SECONDS);

		SMDSearchResponse searchResponse = SMDSearchFactory.getInstance()
				.search(new SMDSearchQuery("*", 0, 1, null));
        assertThat(searchResponse.totalHits, is(0L));

		// add file
		File tmp = File.createTempFile("add-file", "tmp", dir);
		
		new FSSMDRepositoryScan().scrut(fssmdPlugin);

		Thread.sleep(6 * 1000);

        assertThat("We should have a new document indexed", awaitBusy(new Predicate<Object>() {
            @Override
            public boolean apply(Object input) {
                SMDSearchResponse searchResponse = SMDSearchFactory.getInstance().search(new SMDSearchQuery("*", 0, 1, null));
                return searchResponse.totalHits == 1;
            }
        }, 6, TimeUnit.SECONDS), is(true));

		// remove file
		tmp.delete();

		new FSSMDRepositoryScan().scrut(fssmdPlugin);

        assertThat("We should have 0 document indexed", awaitBusy(new Predicate<Object>() {
            @Override
            public boolean apply(Object input) {
                SMDSearchResponse searchResponse = SMDSearchFactory.getInstance().search(new SMDSearchQuery("*", 0, 1, null));
                return searchResponse.totalHits == 0;
            }
        }, 6, TimeUnit.SECONDS), is(true));
	}
}
