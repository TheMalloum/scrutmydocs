package org.scrutmydocs.search;

import com.google.common.base.Predicate;
import org.junit.Test;
import org.scrutmydocs.ScrutMyDocsTests;
import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.domain.SMDSearchQuery;
import org.scrutmydocs.domain.SMDSearchResponse;
import org.scrutmydocs.exceptions.SMDJsonParsingException;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestIndexSearch extends ScrutMyDocsTests {

    @Test
	public void testIndexSearch() throws Exception {

		URL url = getClass().getClassLoader().getResource(TEST_FILE);
		File file = new File(url.toURI());
        assertThat("The test file doesn't exist", file.exists(), is(true));

		SMDDocument smdDocument = components.fileSystemConverter.toDocument(file);

        components.searchService.index(smdDocument);

        assertThat("We should have 1 document indexed", awaitBusy(new Predicate<Object>() {
            @Override
            public boolean apply(Object input) {
                SMDSearchResponse searchResponse = null;
                try {
                    searchResponse = components.searchService.search(new SMDSearchQuery("*", 0, 1, null));
                } catch (SMDJsonParsingException e) {
                    e.printStackTrace();
                }
                return searchResponse.totalHits == 1;
            }
        }, 6, TimeUnit.SECONDS), is(true));

/*
        searchResponse = SMDSearchFactory.getInstance().search(new SMDSearchQuery("*LICENSE*", 0, 1));

        assertThat(searchResponse.totalHits, is(1L));
*/
	}

	@Test
	public void testDeleteAllDocumentsInDirectory() throws Exception {

		URL url = getClass().getClassLoader().getResource(TEST_FILE);
		File file = new File(url.toURI());
        assertThat("The test file doesn't exist", file.exists(), is(true));

        SMDDocument smdDocument = components.fileSystemConverter.toDocument(file);

        components.searchService.index(smdDocument);

        assertThat("We should have 1 document indexed", awaitBusy(new Predicate<Object>() {
            @Override
            public boolean apply(Object input) {
                SMDSearchResponse searchResponse = null;
                try {
                    searchResponse = components.searchService.search(new SMDSearchQuery("*", 0, 1, null));
                } catch (SMDJsonParsingException e) {
                    e.printStackTrace();
                }
                return searchResponse.totalHits == 1;
            }
        }, 6, TimeUnit.SECONDS), is(true));

// TODO Fix
//        searchService.deleteDirectory(smdDocument.file);
        assertThat("We should have 0 document indexed", awaitBusy(new Predicate<Object>() {
            @Override
            public boolean apply(Object input) {
                SMDSearchResponse searchResponse = null;
                try {
                    searchResponse = components.searchService.search(new SMDSearchQuery("*", 0, 1, null));
                } catch (SMDJsonParsingException e) {
                    e.printStackTrace();
                }
                return searchResponse.totalHits == 1;
            }
        }, 6, TimeUnit.SECONDS), is(true));
	}
}
