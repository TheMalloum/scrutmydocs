/*
 * Licensed to scrutmydocs.org (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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

package org.scrutmydocs.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.search.SearchHit;
import org.scrutmydocs.contract.*;
import org.scrutmydocs.elasticsearch.SMDElasticsearchClientFactory;
import org.scrutmydocs.plugins.PluginsUtils;
import org.scrutmydocs.plugins.SMDAbstractPlugin;
import org.scrutmydocs.plugins.fs.FSSMDPlugin;
import org.scrutmydocs.plugins.upload.UploadSMDPlugin;

import java.util.Collection;
import java.util.HashMap;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

class SettingsServiceImpl implements SMDSettingsService {

	private ESLogger logger = Loggers.getLogger(SettingsServiceImpl.class);

	final public static String SMDADMIN = "scrutmydocs-admin";
    final public static String SMDADMIN_SETTINGS = "settings";

	private ObjectMapper mapper = new ObjectMapper();

	private Client esClient;

	public SettingsServiceImpl() {
        this.esClient = SMDElasticsearchClientFactory.getInstance();
        SMDElasticsearchClientFactory.createIndex(SMDADMIN);

        Collection<SMDAbstractPlugin> all = PluginsUtils.getAll();

        for (SMDAbstractPlugin plugin : all) {
            mapper.addMixInAnnotations(plugin.getClass(), SMDAbstractPlugin.class);
            logger.info("  -> adding plugin {}", plugin.name());
        }
    }

	@Override
	public SMDSettings getSettings() {
		try {
			org.elasticsearch.action.search.SearchResponse searchHits = esClient
					.prepareSearch(SMDADMIN).setTypes(SMDADMIN_SETTINGS)
					.execute().actionGet();

			SearchHit searchHit = searchHits.getHits().getAt(0);
            return mapper.readValue(searchHit.getSourceAsString(), SMDSettings.class);
		} catch (Exception e) {
			logger.error("Can not checkout the configuration.");
			throw new RuntimeException("Can not checkout the configuration.");
		}
    }

	@Override
	public void saveSettings(SMDSettings settings) {
		try {
			esClient.prepareIndex(SMDADMIN, SMDADMIN_SETTINGS,
					"settings")
					.setSource(mapper.writeValueAsString(settings))
					.execute().actionGet();
		} catch (Exception e) {
			throw new RuntimeException("Can not save the configuration.");
		}
	}
}
