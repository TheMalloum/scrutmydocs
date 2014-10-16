package org.scrutmydocs.contract;

import org.junit.Test;
import org.scrutmydocs.ScrutMyDocsTests;
import org.scrutmydocs.servlet.ServletInit;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class TestSmdDocument extends ScrutMyDocsTests {

	@Test
	public void testDocumentfromFile() throws Exception {

		URL url = getClass().getClassLoader().getResource(TEST_FILE);
		File file = new File(url.toURI());

        assertThat("The test file doesn't exist", file.exists(), is(true));

		SMDFileDocument smdDocument = new SMDFileDocument(null, file, SMDFileDocument.EXTENSION.TXT);

        assertThat(smdDocument.name, is(file.getName()));
        assertThat(smdDocument.pathDirectory, is(file.getParent()));
        assertThat(smdDocument.url, is(file.getPath()));
	}

	@Test
	public void testDocumentfromInputstream() throws Exception {
		InputStream is = ServletInit.class.getClassLoader().getResourceAsStream(TEST_FILE);
		SMDFileDocument smdDocument = new SMDFileDocument(null, is, "LICENSE", SMDFileDocument.EXTENSION.TXT);

        assertThat(smdDocument, notNullValue());
	}
}
