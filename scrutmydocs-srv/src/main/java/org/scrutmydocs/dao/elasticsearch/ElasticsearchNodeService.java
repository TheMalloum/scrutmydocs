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

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.scrutmydocs.services.SMDConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.AutoStartable;
import restx.factory.Component;

import javax.inject.Inject;

@Component
public class ElasticsearchNodeService implements AutoStartable {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchNodeService.class);

    private final Node esNode;
    private Settings settings = ImmutableSettings.builder().loadFromClasspath("config/es-scrutmydocs.yml").build();

    @Inject
    public ElasticsearchNodeService(SMDConfigurationService configurationService) {
        if (configurationService.getSmdConfiguration().isNodeEmbedded()) {
            logger.debug("Building embedded Node.");
            this.esNode = NodeBuilder.nodeBuilder().settings(settings).build();
            logger.info("Starting embedded node...");
            esNode.start();
        } else {
            esNode = null;
        }
    }

    public Node getEsNode() {
        return esNode;
    }

    @Override
    public void start() {
        if (esNode != null) {
            logger.info("Embedded node started...");
        }
    }
}
