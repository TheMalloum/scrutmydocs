package org.scrutmydocs.scan;

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestScan extends ScrutMyDocsTests {
	File dir;

	@Before
	public void creatTmpDirectory() throws IOException {

		dir = new File(Files.createTempDirectory("srutmydocs").toUri());
	}

	@Test
	public void testScanAdd() throws InterruptedException, IOException, URISyntaxException {

		FSSMDRepositoryData fssmdPlugin = new FSSMDRepositoryData(dir.getAbsolutePath());
		new FSSMDRepositoryScan().scrut(fssmdPlugin);

		Thread.sleep(6 * 1000);

		SMDSearchResponse searchResponse = SMDSearchFactory.getInstance()
				.search(new SMDSearchQuery("*", 0, 1,null));
        assertThat(searchResponse.totalHits, is(0L));

		// add file
		File tmp = File.createTempFile("add-file", "tmp", dir);
		
		new FSSMDRepositoryScan().scrut(fssmdPlugin);

		Thread.sleep(6 * 1000);

		searchResponse = SMDSearchFactory.getInstance().search(new SMDSearchQuery("*", 0, 1,null));
        assertThat(searchResponse.totalHits, is(1L));

		// remove file
		tmp.delete();

		new FSSMDRepositoryScan().scrut(fssmdPlugin);

		Thread.sleep(6 * 1000);

		searchResponse = SMDSearchFactory.getInstance().search(new SMDSearchQuery("*", 0, 1,null));
        assertThat(searchResponse.totalHits, is(0L));
	}
}
