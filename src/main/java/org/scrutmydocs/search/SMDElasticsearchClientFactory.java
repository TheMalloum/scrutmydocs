package org.scrutmydocs.search;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.node.NodeBuilder;

public class SMDElasticsearchClientFactory {

    private static ESLogger logger = Loggers.getLogger(SMDElasticsearchClientFactory.class);
	private static Client client;

    /**
     * We instantiate an elasticsearch backend implementation
     */
	public static synchronized Client getInstance() {
		if (client == null) {
            client = NodeBuilder.nodeBuilder().node().client();
            client.admin()
                .cluster()
                .prepareHealth().setWaitForYellowStatus()
                .execute().actionGet();
        }

		return client;
	}

    public static void createIndex(String index) {
        if (logger.isDebugEnabled())
            logger.debug("createIndex({}, {}, {})", index);

        if (!getInstance().admin().indices().prepareExists(index).execute()
                .actionGet().isExists()) {
            getInstance().admin().indices()
                    .prepareCreate(index).execute();
        }
    }
}
