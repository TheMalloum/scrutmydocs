/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.scrutmydocs.dao.elasticsearch;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.scrutmydocs.configuration.PropertyScanner;
import org.scrutmydocs.configuration.SMDConfiguration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ElasticsearchFactory {

    protected static Logger logger = LogManager.getLogger();

    private static Node esNode = null;
    private static Client esClient = null;
    private static SMDConfiguration smdConfiguration = null;

    public static SMDConfiguration smdConfiguration() {
        if (smdConfiguration != null) {
            return smdConfiguration;
        }
        smdConfiguration = PropertyScanner.scanPropertyFile();
        return smdConfiguration;
    }

    public static Node esNode() {
        SMDConfiguration smdConfiguration = smdConfiguration();

        if (smdConfiguration.isNodeEmbedded()) {
            if (esNode != null) {
                return esNode;
            }

            logger.debug("Building embedded Node.");
            esNode = NodeBuilder.nodeBuilder().node();
            esNode.start();
            logger.info("Starting embedded node...");
            return esNode;
        }

        return null;
    }

    public static Client esClient() {
        if (esClient != null) {
            return esClient;
        }

        SMDConfiguration smdConfiguration = smdConfiguration();


/*
        // TODO Load elasticsearch properties here
        ImmutableSettings.Builder builder = ImmutableSettings.settingsBuilder();
        if (null != this.properties) {
            builder.put(this.properties);
        }
*/

        if (smdConfiguration.isNodeEmbedded()) {
            logger.info("Starting client Node...");
            esClient = esNode().client();
        } else {
            logger.info("Starting client for cluster {} at {} ...", smdConfiguration.getClusterName(), smdConfiguration.getNodeAdresses());
            TransportClient transportClient = new TransportClient();

            for (String address : smdConfiguration.getNodeAdresses()) {
                transportClient.addTransportAddress(toAddress(address));
            }
        }

        // Before returning the client, we need to check if we need to create indices and mappings


        return esClient;
    }

    /**
     * Helper to define an hostname and port with a String like hostname:port
     * @param address Node address hostname:port (or hostname)
     * @return
     */
    private static InetSocketTransportAddress toAddress(String address) {
        if (address == null) return null;

        String[] splitted = address.split(":");
        int port = 9300;
        if (splitted.length > 1) {
            port = Integer.parseInt(splitted[1]);
        }

        return new InetSocketTransportAddress(splitted[0], port);
    }

    public static void createIndex(String index) {
        logger.debug("createIndex({})", index);
        String source = null;
        try {
            source = readFileFromClassLoader("estemplate/" + index + "/_settings.json");
        } catch (IOException e) {
            // We ignore it
        }

        if (!esClient().admin().indices().prepareExists(index).get().isExists()) {

            CreateIndexRequestBuilder builder = esClient().admin().indices().prepareCreate(index);
            if (source != null) {
                builder.setSettings(source);
            }
            builder.get();
        }
   }

    public static void pushMapping(String index, String type) {
        logger.trace("pushMapping({}, {})", index, type);

        if (isMappingExist(index, type)) {
            logger.trace("Mapping [{}]/[{}] already exists.", index, type);
            return;
        }

        // Read the mapping json file if exists and use it
        String source = null;
        try {
            source = readFileFromClassLoader("estemplate/" + index + "/" + type + ".json");
        } catch (IOException e) {
            // We trace it after
        }

        if (source != null) {
            logger.trace("Mapping for [{}]/[{}]=[{}]", index, type, source);

            // Create type and mapping
            PutMappingResponse response = esClient().admin().indices()
                    .preparePutMapping(index).setType(type)
                    .setSource(source).execute().actionGet();
            if (!response.isAcknowledged()) {
                logger.fatal("Mapping [{}/{}] was not pushed.", index, type);
                throw new RuntimeException("Mapping " + index + "/" + type + " was not pushed.");

            }
        } else {
            throw new RuntimeException("Could not find mapping definition for " + index + "/" + type);
        }
    }

    private static String readFileFromClassLoader(String file) throws IOException {
        InputStream asStream = ElasticsearchFactory.class.getClassLoader().getResourceAsStream(file);
        if (asStream == null) {
            throw new FileNotFoundException("Can not find " + file + " in class loader.");
        }
        return IOUtils.toString(asStream);
    }

    /**
     * Check if a mapping already exists in an index
     * @return true if mapping exists
     */
    private static boolean isMappingExist(String index, String type) {
        IndexMetaData imd;
        ClusterState cs = esClient().admin().cluster().prepareState()
                .setIndices(index).execute().actionGet().getState();
        imd = cs.getMetaData().index(index);

        if (imd == null)
            return false;

        MappingMetaData mdd = imd.mapping(type);

        if (mdd != null)
            return true;
        return false;
    }
}
