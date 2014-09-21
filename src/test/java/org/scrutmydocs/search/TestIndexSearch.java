package org.scrutmydocs.search;

import java.io.File;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.plugins.fs.FSSMDPlugin;

public class TestIndexSearch {

	@Test
	public void testIndexSearch() throws Exception {
		
		URL url = getClass().getClassLoader().getResource("es-test.properties");
        File file = new File(url.toURI());
		Assert.assertTrue("The test file doesn't existe",file.exists());
		
		SMDDocument smdDocument = new SMDDocument(file);

		Thread.sleep(5000);

		SMDSearchFactory.getInstance().index(new FSSMDPlugin("url"), smdDocument);
		
		Thread.sleep(5000);
		//TODO when we call the getinstance SMDSearchFactory we have to be sure that indexs are available
		SMDSearchResponse searchResponse = SMDSearchFactory.getInstance().search("*", 1,1);
		
		Assert.assertEquals(1, searchResponse.totalHits);
		
		
		
	}

	@Test
	public void testIndexSearchByDirectory() throws Exception {
		
		URL url = getClass().getClassLoader().getResource("es-test.properties");
        File file = new File(url.toURI());
		Assert.assertTrue("The test file doesn't existe",file.exists());
		
		SMDDocument smdDocument = new SMDDocument(file);

		Thread.sleep(5000);

		SMDSearchFactory.getInstance().index(new FSSMDPlugin("url"), smdDocument);
		
		Thread.sleep(5000);
		//TODO when we call the getinstance SMDSearchFactory we have to be sure that indexs are available
		SMDSearchResponse searchResponse = SMDSearchFactory.getInstance().searchFileByDirectory(smdDocument.pathDirectory, 1,1);
		
		Assert.assertEquals(1, searchResponse.totalHits);
		
		
		
	}
	
	

	@Test
	public void testdelete() throws Exception {
		
		URL url = getClass().getClassLoader().getResource("es-test.properties");
        File file = new File(url.toURI());
		Assert.assertTrue("The test file doesn't existe",file.exists());
		
		SMDDocument smdDocument = new SMDDocument(file);

		Thread.sleep(5000);

		SMDSearchFactory.getInstance().index(new FSSMDPlugin("url"), smdDocument);
		
		Thread.sleep(5000);
		//TODO when we call the getinstance SMDSearchFactory we have to be sure that indexs are available
		SMDSearchFactory.getInstance().index(new FSSMDPlugin("url"), smdDocument);
		
		Thread.sleep(5000);
		//TODO when we call the getinstance SMDSearchFactory we have to be sure that indexs are available
		SMDSearchResponse searchResponse = SMDSearchFactory.getInstance().search("*", 1,1);
		
		Assert.assertEquals(1, searchResponse.totalHits);
		
		
	}
	
}
