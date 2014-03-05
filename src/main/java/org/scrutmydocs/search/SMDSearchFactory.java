package org.scrutmydocs.search;

import org.scrutmydocs.contract.SMDDataSource;
import org.scrutmydocs.contract.SMDsearch;

public class SMDSearchFactory {

	private static SMDsearch smDsearch;

	public static synchronized SMDsearch getInstance(SMDDataSource smdDataSource) {

		if (smDsearch == null) {
			smDsearch = new ESSearchService(smdDataSource);
		}
		return smDsearch;
	}

}
