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

import fr.pilato.elasticsearch.tools.ElasticsearchBeyonder;
import fr.pilato.elasticsearch.tools.index.IndexFinder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.indices.IndexMissingException;
import org.scrutmydocs.domain.SMDConfiguration;
import org.scrutmydocs.services.SMDConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.AutoStartable;
import restx.factory.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

@Component
public class ElasticsearchClientService implements AutoStartable {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchClientService.class);

    private static final String templateDirName = "estemplate";

    private final Client esClient;
    private final SMDConfiguration smdConfiguration;

    @Inject
    public ElasticsearchClientService(SMDConfigurationService configurationService,
                                      ElasticsearchNodeService nodeService) {
        this.smdConfiguration = configurationService.getSmdConfiguration();
/*
        // TODO Load elasticsearch properties here
        ImmutableSettings.Builder builder = ImmutableSettings.settingsBuilder();
        if (null != this.properties) {
            builder.put(this.properties);
        }
*/

        if (this.smdConfiguration.isNodeEmbedded()) {
            logger.debug("Starting client Node...");
            esClient = nodeService.getEsNode().client();
        } else {
            logger.debug("Starting client for cluster {} at {} ...", this.smdConfiguration.getClusterName(),
                    this.smdConfiguration.getNodeAdresses());
            TransportClient transportClient = new TransportClient();

            for (String address : this.smdConfiguration.getNodeAdresses()) {
                transportClient.addTransportAddress(toAddress(address));
            }
            esClient = transportClient;
        }
    }

    @Override
    public void start() {
        logger.debug("Client Node started. Checking elasticsearch config...");

        // Let's remove automatically indices and mappings if CleanOnStart is set
        if (smdConfiguration.isCleanOnStart()) {
            try {
                Collection<String> indices = IndexFinder.findIndexNames(templateDirName);
                // For each we remove it if needed
                for (String index : indices) {
                    logger.warn("/!\\ Clean On Start activated /!\\. Removing index [{}]", index);
                    try {
                        esClient.admin().indices().prepareDelete(index).get();
                    } catch (IndexMissingException e) {
                        // We ignore if the index was not existing before
                    }
                }
            } catch (IOException|URISyntaxException e) {
                logger.warn("Can not read [{}]...", templateDirName);
            }
        }

        try {
            ElasticsearchBeyonder.start(esClient, templateDirName);
        } catch (Exception e) {
            logger.error("Can not setup elasticsearch", e);
        }

        logger.info("Elasticsearch config checked... Rock & roll!");
    }

    public Client getEsClient() {
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
}
