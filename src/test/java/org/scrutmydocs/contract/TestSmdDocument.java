package org.scrutmydocs.contract;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.scrutmydocs.servlet.ServletInit;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestSmdDocument {

	@Test
	public void testDocumentfromFile() throws Exception {

		URL url = getClass().getClassLoader().getResource("es-test.properties");
		File file = new File(url.toURI());

		Assert.assertTrue("The test file doesn't existe", file.exists());

		SMDDocument smdDocument = new SMDDocument(file);

		Assert.assertEquals(file.getName(), smdDocument.name);
		Assert.assertEquals(file.getParent(), smdDocument.pathDirectory);
		Assert.assertEquals(file.getPath(), smdDocument.url);

	}

	@Test
	public void testDocumentfromInputstream() throws Exception {

		InputStream is = ServletInit.class.getClassLoader()
				.getResourceAsStream("LICENSE");
		SMDDocument smdDocument = new SMDDocument(is, "LICENSE");

		Assert.assertNotNull(smdDocument);

	}

	

}
