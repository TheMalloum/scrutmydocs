package org.scrutmydocs.contract;

import org.junit.Test;
import org.scrutmydocs.ScrutMyDocsTests;
import org.scrutmydocs.domain.SMDConfiguration;
import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.services.SMDDocumentService;
import org.scrutmydocs.services.SMDRepositoriesService;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.scrutmydocs.converters.FileToSMDDocument.*;

public class TestSmdDocument extends ScrutMyDocsTests {

    @Inject
    public TestSmdDocument(SMDRepositoriesService repositoriesService,
                           SMDDocumentService searchService) {
        super(repositoriesService, searchService);
    }

    @Test
	public void testDocumentfromFile() throws Exception {

		URL url = getClass().getClassLoader().getResource(TEST_FILE);
		File file = new File(url.toURI());

        assertThat("The test file doesn't exist", file.exists(), is(true));

		SMDDocument smdDocument = toDocument(file);

        assertThat(smdDocument.file.filename, is(file.getName()));
        assertThat(smdDocument.file.path, is(file.getParent()));
        assertThat(smdDocument.file.url, is(file.getPath()));
	}

	@Test
	public void testDocumentfromInputstream() throws Exception {
		InputStream is = TestSmdDocument.class.getResourceAsStream(TEST_FILE);
		String path = new File(TestSmdDocument.class.getResource(TEST_FILE).getFile()).getParent();
        SMDDocument smdDocument = toDocument(is, TYPE_FS, SMDConfiguration.INDEXED_CHARS_DEFAULT, TEST_FILENAME, path,
                null, new Date(), null, null);
        assertThat(smdDocument, notNullValue());
	}
}
