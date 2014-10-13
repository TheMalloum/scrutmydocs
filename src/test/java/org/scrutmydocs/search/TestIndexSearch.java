package org.scrutmydocs.search;

import java.io.File;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.scrutmydocs.contract.SMDFileDocument;
import org.scrutmydocs.contract.SMDSearchQuery;
import org.scrutmydocs.contract.SMDSearchResponse;

public class TestIndexSearch {

	@Test
	public void testIndexSearch() throws Exception {

		URL url = getClass().getClassLoader().getResource("es-test.properties");
		File file = new File(url.toURI());
		Assert.assertTrue("The test file doesn't existe", file.exists());

		SMDFileDocument smdDocument = new SMDFileDocument(null,file, null);

		Thread.sleep(5000);

		SMDSearchFactory.getInstance().index(smdDocument);

		Thread.sleep(5000);
		// TODO when we call the getinstance SMDSearchFactory we have to be sure
		// that indexs are available
		SMDSearchResponse searchResponse = SMDSearchFactory.getInstance()
				.search(new SMDSearchQuery("*", 0, 1));

		Assert.assertEquals(1, searchResponse.totalHits);

	}

	@Test
	public void testdeleteAllDocumentsInDirectory() throws Exception {

		URL url = getClass().getClassLoader().getResource("es-test.properties");
		File file = new File(url.toURI());
		Assert.assertTrue("The test file doesn't existe", file.exists());

		SMDFileDocument smdDocument = new SMDFileDocument(null,file, "upload");

		SMDSearchFactory.getInstance().index(smdDocument);

		Thread.sleep(6000);

		SMDSearchFactory.getInstance().deleteDirectory(smdDocument.pathDirectory);

		
		Thread.sleep(6000);
		
		SMDSearchResponse searchResponse = SMDSearchFactory.getInstance()
				.search(new SMDSearchQuery("*", 0, 1));
		
		Assert.assertEquals(0, searchResponse.totalHits);

	}

	@Test
	public void testdelete() throws Exception {

		URL url = getClass().getClassLoader().getResource("es-test.properties");
		File file = new File(url.toURI());
		Assert.assertTrue("The test file doesn't existe", file.exists());

		SMDFileDocument smdDocument = new SMDFileDocument(null,file, null);

		Thread.sleep(5000);

		SMDSearchFactory.getInstance().index(smdDocument);

		Thread.sleep(5000);
		// TODO when we call the getinstance SMDSearchFactory we have to be sure
		// that indexs are available
		SMDSearchFactory.getInstance().index(smdDocument);

		Thread.sleep(5000);
		// TODO when we call the getinstance SMDSearchFactory we have to be sure
		// that indexs are available
		SMDSearchResponse searchResponse = SMDSearchFactory.getInstance()
				.search(new SMDSearchQuery("*", 0, 1));

		Assert.assertEquals(1, searchResponse.totalHits);

	}

}
