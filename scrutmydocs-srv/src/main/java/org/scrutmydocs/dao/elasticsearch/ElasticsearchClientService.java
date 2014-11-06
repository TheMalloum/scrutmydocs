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

import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.indices.IndexMissingException;
import org.scrutmydocs.domain.SMDConfiguration;
import org.scrutmydocs.services.SMDConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.AutoStartable;
import restx.factory.Component;

import javax.inject.Inject;
import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class ElasticsearchClientService implements AutoStartable {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchClientService.class);

    private static final String templateDirName = "estemplate";
    private static final String jsonExtension = ".json";
    private static final String indexSettingsFileName = "_settings" + jsonExtension;
    private static final File templateDir = new File(ElasticsearchService.class.getClassLoader()
            .getResource(templateDirName).getFile());

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

        // Let's create automatically indices and mappings
        Collection<String> indices = getExistingIndices();

        // For each we create it if needed
        for (String index : indices) {
            String indexName = Files.getNameWithoutExtension(index);
            logger.debug("looking for index [{}]", indexName);

            if (smdConfiguration.isCleanOnStart()) {
                logger.warn("/!\\ Clean On Start activated /!\\. Removing index [{}]", indexName);
                try {
                    esClient.admin().indices().prepareDelete(indexName).get();
                } catch (IndexMissingException e) {
                    // We ignore if the index was not existing before
                }
            }

            // If index does not exist, let's create it
            boolean indexExists = esClient.admin().indices().prepareExists(indexName).get().isExists();
            if (indexExists) {
                logger.debug("Index [{}] already exists. Skipping.", indexName);
            } else {
                File settings = getExistingSettings(indexName);
                if (settings != null) {
                    try {
                        Settings indexSettings = ImmutableSettings.builder()
                                .loadFromUrl(settings.toPath().toUri().toURL())
                                .build();
                        logger.trace("Settings for [{}]: [{}]", indexName, indexSettings.toDelimitedString(','));

                        esClient.admin().indices().prepareCreate(indexName).setSettings(indexSettings).get();
                        logger.debug("Index [{}] created.", indexName);
                    } catch (MalformedURLException e) {
                        logger.warn("Can not read settings from [{}]", settings);
                    }
                }
            }

            Collection<String> types = getExistingMappings(index);
            for (String type : types) {
                String mappingName = Files.getNameWithoutExtension(type);
                logger.debug("looking for mapping [{}/{}]", indexName, mappingName);

                // If type does not exist, let's create it
                GetMappingsResponse response = esClient.admin().indices()
                        .prepareGetMappings(indexName).addTypes(mappingName).get();
                boolean mappingExists = (response.getMappings().containsKey(indexName) &&
                        response.getMappings().get(indexName).containsKey(mappingName));
                if (mappingExists) {
                    logger.debug("Type [{}/{}] already exists. Skipping.", indexName, mappingName);
                } else {
                    String mapping = getExistingMapping(indexName, mappingName);
                    if (mapping != null) {
                        logger.trace("Mappings for [{}/{}]: [{}]", indexName, mappingName, mapping);
                        esClient.admin().indices().preparePutMapping(indexName)
                            .setType(mappingName).setSource(mapping).get();
                        logger.debug("Mapping [{}/{}] created.", indexName, mappingName);
                    }
                }
            }
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

    private static Collection<String> getExistingIndices() {
        logger.trace("Reading dirs in [{}]...", templateDirName);
        Collection<String> indices = new ArrayList<>();
        if (!templateDir.exists()) {
            logger.debug("[{}] dir does not exist...", templateDirName);
            return indices;
        }
        if (!templateDir.isDirectory()) {
            logger.warn("[{}] is not a directory...", templateDirName);
            return indices;
        }
        File[] files = templateDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                logger.trace("added [{}]...", file);
                indices.add(file.getName());
            } else {
                logger.trace("ignoring [{}]...", file);
            }
        }

        return indices;
    }

    private static Collection<String> getExistingMappings(String index) {
        logger.trace("Reading mappings in [{}]...", index);
        Collection<String> mappings = new ArrayList<>();
        File dir = new File(templateDir, index);
        if (!dir.exists()) {
            logger.debug("[{}] dir does not exist...", index);
            return mappings;
        }
        if (!dir.isDirectory()) {
            logger.warn("[{}] is not a directory...", index);
            return mappings;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (!file.isDirectory() && !file.getName().equals(indexSettingsFileName)) {
                logger.trace("added [{}]...", file);
                mappings.add(file.getName());
            } else {
                logger.trace("ignoring [{}]...", file);
            }
        }

        return mappings;
    }

    private static File getExistingSettings(String index) {
        logger.trace("Reading settings for [{}]...", index);
        File dir = new File(templateDir, index);
        File settings = new File(dir, indexSettingsFileName);
        if (!settings.exists()) {
            logger.debug("[{}/{}] does not exist at [{}].", index, indexSettingsFileName, settings);
            return null;
        }
        if (!settings.isFile()) {
            logger.warn("[{}/{}] is not a file at [{}]!", index, indexSettingsFileName, settings);
            return null;
        }

        return settings;
    }

    /**
     * Read a mapping
     * @param index index name
     * @param type type name (.json will be appended)
     * @return
     */
    private static String getExistingMapping(String index, String type) {
        logger.trace("Reading mapping for [{}/{}]...", index, type);
        File dir = new File(templateDir, index);
        File settings = new File(dir, type + jsonExtension);
        if (!settings.exists()) {
            logger.debug("[{}/{}] does not exist at [{}].", index, type, settings);
            return null;
        }
        if (!settings.isFile()) {
            logger.warn("[{}/{}] is not a file at [{}]!", index, type, settings);
            return null;
        }

        String mapping = null;

        logger.trace("Reading file [{}]...", settings);
        try (InputStream asStream = new FileInputStream(settings)) {
            if (asStream == null) {
                logger.warn("Can not find [{}] in class loader.", settings);
                return null;
            }
            mapping = IOUtils.toString(asStream);
        } catch (IOException e) {
            logger.warn("Can not read [{}].", settings);
        }

        return mapping;
    }
}
