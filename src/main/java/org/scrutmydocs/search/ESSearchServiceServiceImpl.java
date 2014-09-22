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

package org.scrutmydocs.search;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.contract.SMDResponseDocument;
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.contract.SMDSearchService;
import org.scrutmydocs.contract.SMDSettingsService;
import org.scrutmydocs.plugins.PluginsUtils;
import org.scrutmydocs.plugins.SMDAbstractPlugin;

import com.fasterxml.jackson.databind.ObjectMapper;

class ESSearchServiceServiceImpl implements SMDSearchService,
		SMDSettingsService {

	protected Logger logger = LogManager.getLogger();

	final public static String SMDINDEX = "scrutmydocs-docs";
	final public static String SMDADMIN = "scrutmydocs-admin";
	final public static String SMDADMIN_SETTINGS = "settings";

	private Client esClient;

	private BulkProcessor bulk;

	ObjectMapper mapper = new ObjectMapper();

	public ESSearchServiceServiceImpl() {
		SMDElasticsearchClientFactory.createIndex(SMDINDEX);
		SMDElasticsearchClientFactory.createIndex(SMDADMIN);

		this.esClient = SMDElasticsearchClientFactory.getInstance();

		Collection<SMDAbstractPlugin> all = PluginsUtils.getAll().values();

		for (SMDAbstractPlugin plugin : all) {
			mapper.addMixInAnnotations(plugin.getClass(),
					SMDAbstractPlugin.class);
			logger.info("  -> adding plugin {}", plugin.name());
		}

		this.bulk = new BulkProcessor.Builder(esClient,
				new BulkProcessor.Listener() {
					@Override
					public void beforeBulk(long l, BulkRequest bulkRequest) {
						logger.debug("before bulking {} actions",
								bulkRequest.numberOfActions());
					}

					@Override
					public void afterBulk(long l, BulkRequest bulkRequest,
							BulkResponse bulkItemResponses) {
						logger.debug("after bulking {} actions: failures: {}",
								bulkRequest.numberOfActions(),
								bulkItemResponses.hasFailures());
						// TODO check if errors
					}

					@Override
					public void afterBulk(long l, BulkRequest bulkRequest,
							Throwable throwable) {
						logger.warn("Error while executing bulk", throwable);
					}
				}).setFlushInterval(TimeValue.timeValueSeconds(5))
				.setBulkActions(100).build();
	}

	@Override
	public SMDSearchResponse search(String search, int first, int pageSize) {
		if (logger.isDebugEnabled())
			logger.debug("google('{}', {}, {})", search, first, pageSize);

		long totalHits = -1;
		long took = -1;

		SMDSearchResponse searchResponse = null;

		QueryBuilder qb;
		if (search == null || search.trim().length() <= 0) {
			qb = matchAllQuery();
		} else {
			qb = queryString(search);
		}

		org.elasticsearch.action.search.SearchResponse searchHits = esClient
				.prepareSearch().setIndices(SMDINDEX)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb)
				.setFrom(first).setSize(pageSize).addHighlightedField("name")
				.addHighlightedField("file")
				.setHighlighterPreTags("<span class='badge badge-info'>")
				.setHighlighterPostTags("</span>").addFields("*", "_source")
				.execute().actionGet();

		totalHits = searchHits.getHits().totalHits();
		took = searchHits.getTookInMillis();

		List<SMDResponseDocument> documents = new ArrayList<SMDResponseDocument>();
		for (SearchHit searchHit : searchHits.getHits()) {

			Collection<String> highlights = null;
			if (searchHit.getHighlightFields() != null) {
				highlights = new ArrayList<String>();
				for (HighlightField highlightField : searchHit
						.getHighlightFields().values()) {

					Text[] fragmentsBuilder = highlightField.getFragments();

					for (Text fragment : fragmentsBuilder) {
						highlights.add(fragment.string());
					}
				}
			}

			SMDResponseDocument smdResponseDocument = new SMDResponseDocument(
					(String) searchHit.getSource().get("name"),
					(String) searchHit.getSource().get("url"),
					(String) searchHit.getSource().get("contentType"),
					highlights);
			documents.add(smdResponseDocument);
		}

		searchResponse = new SMDSearchResponse(took, totalHits, documents);

		if (logger.isDebugEnabled())
			logger.debug("/google({}) : {}", search, totalHits);

		return searchResponse;

	}

	@Override
	public void index(SMDAbstractPlugin smdAbstractPlugin, SMDDocument document) {

		if (logger.isDebugEnabled())
			logger.debug("index({})", document);

		try {

			String json = mapper.writeValueAsString(document);

			bulk.add(new IndexRequest(SMDINDEX, smdAbstractPlugin.name(),
					document.url).source(json));
		} catch (Exception e) {
			logger.warn("Can not index document {}", document.name);
			throw new RuntimeException("Can not index document : "
					+ document.name + ": " + e.getMessage());
		}

		if (logger.isDebugEnabled())
			logger.debug("/index()={}", document);

	}

	@Override
	public void delete(SMDAbstractPlugin smdAbstractPlugin, String id) {

		if (logger.isDebugEnabled())
			logger.debug("delete({})", id);

		if (id==null || id.isEmpty()) {
			throw new IllegalArgumentException(
					"The id of document can't be null or empty");
		}

		try {
			bulk.add(new DeleteRequest(SMDINDEX, smdAbstractPlugin.url, id));
		} catch (Exception e) {
			logger.warn("Can not delete document {} of type  {}", id,
					smdAbstractPlugin.url);
			throw new RuntimeException("Can not delete document : " + id
					+ "whith type " + smdAbstractPlugin.url + ": "
					+ e.getMessage());
		}

		if (logger.isDebugEnabled())
			logger.debug("/delete()={}", id);

	}

	@Override
	public SMDSearchResponse searchFileByDirectory(SMDAbstractPlugin smdAbstractPlugin,String directory, int first,
			int pageSize) {
		if (logger.isDebugEnabled())
			logger.debug("searchFileByDirectory('{}', {}, {})", directory,
					first, pageSize);

		long totalHits = -1;
		long took = -1;

		SMDSearchResponse searchResponse = null;

		BoolFilterBuilder filters = FilterBuilders.boolFilter().must(
				FilterBuilders.termFilter("pathDirectory", directory));
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		QueryBuilder query = QueryBuilders.filteredQuery(qb, filters);

		org.elasticsearch.action.search.SearchResponse searchHits = esClient
				.prepareSearch().setIndices(SMDINDEX)
				.setSearchType(smdAbstractPlugin.name()).setQuery(query)
				.setFrom(first).setSize(pageSize).execute().actionGet();

		totalHits = searchHits.getHits().totalHits();
		took = searchHits.getTookInMillis();

		List<SMDResponseDocument> documents = new ArrayList<SMDResponseDocument>();
		for (SearchHit searchHit : searchHits.getHits()) {

			Collection<String> highlights = null;
			if (searchHit.getHighlightFields() != null) {
				highlights = new ArrayList<String>();
				for (HighlightField highlightField : searchHit
						.getHighlightFields().values()) {

					Text[] fragmentsBuilder = highlightField.getFragments();

					for (Text fragment : fragmentsBuilder) {
						highlights.add(fragment.string());
					}
				}
			}

			SMDResponseDocument smdResponseDocument = new SMDResponseDocument(
					(String) searchHit.getSource().get("name"),
					(String) searchHit.getSource().get("url"),
					(String) searchHit.getSource().get("contentType"),
					highlights);

			documents.add(smdResponseDocument);
		}

		searchResponse = new SMDSearchResponse(took, totalHits, documents);

		if (logger.isDebugEnabled())
			logger.debug("searchFileByDirectory('{}', {}, {})", directory,
					first, pageSize);
		return searchResponse;

	}

	// Settings

	@Override
	public List<SMDAbstractPlugin> getSettings() {
		try {
			org.elasticsearch.action.search.SearchResponse searchHits = esClient
					.prepareSearch(SMDADMIN).setTypes(SMDADMIN_SETTINGS)
					.execute().actionGet();

			if (searchHits.getHits().totalHits() == 0) {
				return null;
			}

			List<SMDAbstractPlugin> plugins = new ArrayList<SMDAbstractPlugin>();
			for (SearchHit searchHit : searchHits.getHits()) {
				plugins.add(mapper.readValue(
						searchHit.getSourceAsString(),
						PluginsUtils.getAll()
								.get(searchHit.getSource().get("name"))
								.getClass()));
			}

			return plugins;
		} catch (Exception e) {
			logger.error("Can not checkout the configuration.");
			throw new RuntimeException("Can not checkout the configuration.");
		}
	}

	@Override
	public void saveSetting(SMDAbstractPlugin setting) {
		try {
			esClient.prepareIndex(SMDADMIN, SMDADMIN_SETTINGS, "settings")
					.setSource(mapper.writeValueAsString(setting)).execute()
					.actionGet();
		} catch (Exception e) {
			throw new RuntimeException("Can not save the configuration.");
		}
	}

	@Override
	public SMDAbstractPlugin getSetting(String id) {
		try {
			GetResponse response = esClient
					.prepareGet(SMDADMIN, SMDADMIN_SETTINGS,id)
					.setOperationThreaded(false).execute().actionGet();

			if(!response.isExists()) return null;
			
			return mapper.readValue(response.getSourceAsString(), PluginsUtils
					.getAll().get(response.getSource().get("name")).getClass());
		} catch (Exception e) {
			throw new RuntimeException("Can not save the configuration.");
		}

	}

}
