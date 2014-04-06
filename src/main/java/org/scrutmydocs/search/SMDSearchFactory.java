package org.scrutmydocs.search;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.NodeBuilder;
import org.scrutmydocs.contract.SMDsearch;

public class SMDSearchFactory {

	private static SMDsearch smDsearch;

	public static synchronized SMDsearch getInstance() {

		if (smDsearch== null) {
			smDsearch = new ESSearchService();
		}
		return smDsearch;
	}
	
}
