package org.scrutmydocs.search;

import org.scrutmydocs.search.data.SMDsearch;

public class SearchFactory {

	private static SMDsearch smDsearch;

	public static synchronized SMDsearch getInstance() {

		if (smDsearch == null) {
			smDsearch = new ESSearchService();

		}
		return smDsearch;
	}

}
