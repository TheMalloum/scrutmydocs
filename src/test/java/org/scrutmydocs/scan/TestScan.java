package org.scrutmydocs.scan;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.plugins.fs.FSSMDPlugin;
import org.scrutmydocs.search.SMDSearchFactory;

public class TestScan {
	File dir;

	@Before
	public void creatTmpDirectory() throws IOException {

		dir = new File(Files.createTempDirectory("srutmydocs").toUri());
	}
	
	@Test
	public void testScanAdd() throws InterruptedException, IOException, URISyntaxException {

		FSSMDPlugin fssmdPlugin = new FSSMDPlugin(dir.getAbsolutePath());

		fssmdPlugin.scrut();

		Thread.sleep(6 * 1000);

		SMDSearchResponse searchResponse = SMDSearchFactory.getInstance()
				.search("*", 0, 1);

		Assert.assertEquals(0, searchResponse.totalHits);

		
		
		// add file
		
		File tmp = File.createTempFile("add-file", "tmp", dir);
		
		fssmdPlugin.scrut();

		Thread.sleep(6 * 1000);

		searchResponse = SMDSearchFactory.getInstance().search("*", 1, 1);

		Assert.assertEquals(1, searchResponse.totalHits);

		// remove file
		tmp.delete();

		fssmdPlugin.scrut();

		Thread.sleep(6 * 1000);

		searchResponse = SMDSearchFactory.getInstance().search("*", 1, 1);

		Assert.assertEquals(0, searchResponse.totalHits);

	}
}
