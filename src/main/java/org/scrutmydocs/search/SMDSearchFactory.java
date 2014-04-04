package org.scrutmydocs.search;

import org.scrutmydocs.contract.SMDsearch;
import org.scrutmydocs.datasource.SMDDataSource;

public class SMDSearchFactory {

	private static SMDsearch smDsearch;

	public static synchronized SMDsearch getInstance(SMDDataSource smdDataSource) {

		if (smDsearch == null) {
			smDsearch = new ESSearchService(smdDataSource);
		}
		return smDsearch;
	}

}
