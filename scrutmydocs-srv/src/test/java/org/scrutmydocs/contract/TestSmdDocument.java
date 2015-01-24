package org.scrutmydocs.contract;

import org.junit.Test;
import org.scrutmydocs.ScrutMyDocsTests;
import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.plugins.fs.FileSystemPlugin;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class TestSmdDocument extends ScrutMyDocsTests {

    @Test
	public void testDocumentfromFile() throws Exception {

		URL url = getClass().getClassLoader().getResource(TEST_FILE);
		File file = new File(url.toURI());

        assertThat("The test file doesn't exist", file.exists(), is(true));

		SMDDocument smdDocument = components.fileSystemConverter.toDocument(file);

        assertThat(smdDocument.file.filename, is(file.getName()));
        assertThat(smdDocument.file.path, is(file.getParent()));
        assertThat(smdDocument.file.url, is("file://" + file.getPath()));
	}

	@Test
	public void testDocumentfromInputstream() throws Exception {
		InputStream is = TestSmdDocument.class.getResourceAsStream("/" + TEST_FILE);
		String path = new File(TestSmdDocument.class.getResource("/" + TEST_FILE).getFile()).getParent();
        SMDDocument smdDocument = components.fileSystemConverter.toDocument(is, TEST_FILENAME, FileSystemPlugin.TYPE_FS, TEST_FILENAME, path,
                null, new Date(), null, null);
        assertThat(smdDocument, notNullValue());
	}

	@Test
	public void testJsonToSMDDocument() throws Exception {
		String json = "{\n" +
				"   \"id\":\"ZYRfBGwm8j6/0yvkikdwm7qJLaM=\",\n" +
				"   \"type\":\"fs\",\n" +
				"   \"content\":\"\",\n" +
				"   \"meta\":{\n" +
				"      \"author\":null,\n" +
				"      \"title\":null,\n" +
				"      \"date\":null,\n" +
				"      \"keywords\":[\n" +
				"\n" +
				"      ]\n" +
				"   },\n" +
				"   \"file\":{\n" +
				"      \"content_type\":\"application/octet-stream\",\n" +
				"      \"last_modified\":1411037363000,\n" +
				"      \"indexing_date\":1422115405189,\n" +
				"      \"filesize\":6148,\n" +
				"      \"filename\":\".DS_Store\",\n" +
				"      \"path\":\"/Users/dpilato/Documents/Elasticsearch/tmp/es\",\n" +
				"      \"url\":\"file:///Users/dpilato/Documents/Elasticsearch/tmp/es/.DS_Store\",\n" +
				"      \"indexed_chars\":null\n" +
				"   }\n" +
				"}";
		SMDDocument smdDocument = components.jsonToSMDDocumentService.toDocument(json);

	}


}
