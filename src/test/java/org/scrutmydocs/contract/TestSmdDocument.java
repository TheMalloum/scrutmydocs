package org.scrutmydocs.contract;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.scrutmydocs.contract.SMDDocument;

public class TestSmdDocument {

	@Test
	public void testIndexSearch() throws Exception {
		
		URL url = getClass().getClassLoader().getResource("es-test.properties");
        File file = new File(url.toURI());

		Assert.assertTrue("The test file doesn't existe",file.exists());
		
		SMDDocument smdDocument = new SMDDocument(file);
		
		Assert.assertEquals(file.getName(), smdDocument.name);
		Assert.assertEquals(file.getParent(), smdDocument.pathDirectory);
		Assert.assertEquals(file.getPath(), smdDocument.url);

	}
}
