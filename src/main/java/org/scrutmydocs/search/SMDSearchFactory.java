package org.scrutmydocs.search;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.NodeBuilder;
import org.scrutmydocs.contract.SMDsearch;
import org.scrutmydocs.datasource.SMDDataSource;

public class SMDSearchFactory {

	private static Client client;

	public static synchronized SMDsearch getInstance(SMDDataSource smdDataSource) {

		if (client == null) {
			client = NodeBuilder.nodeBuilder().node().client();
		}
		return new ESSearchService(smdDataSource, client);
	}
}
