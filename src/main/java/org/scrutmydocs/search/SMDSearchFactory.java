package org.scrutmydocs.search;

import org.scrutmydocs.contract.SMDSearchService;

public class SMDSearchFactory {

	private static SMDSearchService SMDSearchService;

    /**
     * We instantiate an elasticsearch backend implementation
     */
	public static synchronized SMDSearchService getInstance() {
		if (SMDSearchService == null) {
			SMDSearchService = new ElasticSearchImpl();
		}

		return SMDSearchService;
	}
	
}
