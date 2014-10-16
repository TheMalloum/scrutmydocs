package org.scrutmydocs.search;

import org.junit.Test;
import org.scrutmydocs.ScrutMyDocsTests;
import org.scrutmydocs.contract.SMDFileDocument;
import org.scrutmydocs.contract.SMDSearchQuery;
import org.scrutmydocs.contract.SMDSearchResponse;

import java.io.File;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestIndexSearch extends ScrutMyDocsTests {

	@Test
	public void testIndexSearch() throws Exception {

		URL url = getClass().getClassLoader().getResource(TEST_FILE);
		File file = new File(url.toURI());
        assertThat("The test file doesn't exist", file.exists(), is(true));

		SMDFileDocument smdDocument = new SMDFileDocument(null, file, SMDFileDocument.EXTENSION.TXT);

		Thread.sleep(5000);

		SMDSearchFactory.getInstance().index(smdDocument);

		Thread.sleep(6000);
		// TODO when we call the getinstance SMDSearchFactory we have to be sure
		// that indexs are available
		SMDSearchResponse searchResponse = SMDSearchFactory.getInstance().search(new SMDSearchQuery("*", 0, 1));

        assertThat(searchResponse.totalHits, is(1L));

        searchResponse = SMDSearchFactory.getInstance().search(new SMDSearchQuery("*LICENSE*", 0, 1));

        assertThat(searchResponse.totalHits, is(1L));
	}

	@Test
	public void testDeleteAllDocumentsInDirectory() throws Exception {

		URL url = getClass().getClassLoader().getResource(TEST_FILE);
		File file = new File(url.toURI());
        assertThat("The test file doesn't exist", file.exists(), is(true));

		SMDFileDocument smdDocument = new SMDFileDocument(null, file, "upload");

		SMDSearchFactory.getInstance().index(smdDocument);

		Thread.sleep(6000);

		SMDSearchFactory.getInstance().deleteDirectory(smdDocument.pathDirectory);

		Thread.sleep(6000);
		
		SMDSearchResponse searchResponse = SMDSearchFactory.getInstance()
				.search(new SMDSearchQuery("*", 0, 1));

        assertThat(searchResponse.totalHits, is(0L));
	}
}
