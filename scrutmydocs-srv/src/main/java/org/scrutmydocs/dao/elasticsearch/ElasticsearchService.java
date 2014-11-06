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

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.scrutmydocs.services.SMDConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Component;

@Component
public class ElasticsearchService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchService.class);

    private final ElasticsearchNodeService nodeService;
    private final ElasticsearchClientService clientService;
    private final SMDConfigurationService configurationService;

    public ElasticsearchService(ElasticsearchNodeService nodeService,
                                SMDConfigurationService configurationService,
                                ElasticsearchClientService clientService) {
        this.nodeService = nodeService;
        this.clientService = clientService;
        this.configurationService = configurationService;
    }

    public Node esNode() {
        return nodeService.getEsNode();
    }

    public Client esClient() {
        return clientService.getEsClient();
    }
}
