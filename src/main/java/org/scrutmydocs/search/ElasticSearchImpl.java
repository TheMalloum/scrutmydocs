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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.contract.SMDResponseDocument;
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.contract.SMDSearchService;
import org.scrutmydocs.contract.SMDRepositoriesService;
import org.scrutmydocs.repositories.PluginsUtils;
import org.scrutmydocs.repositories.SMDAbstractRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticSearchImpl implements SMDSearchService, SMDRepositoriesService {

	protected Logger logger = LogManager.getLogger();

	final public static String SMDINDEX = "scrutmydocs-docs";
	final public static String SMDADMIN = "scrutmydocs-admin";
	final public static String SMDADMIN_REPOSITORIES = "repositories";

	private Client esClient;

	private BulkProcessor bulk;

	ObjectMapper mapper = new ObjectMapper();

	public ElasticSearchImpl() {
		esClient = NodeBuilder.nodeBuilder().node().client();
		esClient.admin().cluster().prepareHealth().setWaitForYellowStatus()
				.execute().actionGet();
		
		
		createIndex(SMDINDEX);
		createIndex(SMDADMIN);

		

		Collection<SMDAbstractRepository> all = PluginsUtils.getAll().values();

		for (SMDAbstractRepository plugin : all) {
			mapper.addMixInAnnotations(plugin.getClass(),
					SMDAbstractRepository.class);
			logger.info("  -> adding plugin {}", plugin.type);
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
						logger.debug("after bulking {} actions- has failure failures: {}",
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


	public void createIndex(String index) {
		if (logger.isDebugEnabled())
			logger.debug("createIndex({}, {}, {})", index);

		if (!esClient.admin().indices().prepareExists(index).execute()
				.actionGet().isExists()) {
			esClient.admin().indices().prepareCreate(index).execute();
		}
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
				.setFrom(first).setSize(pageSize).addHighlightedField("type")
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
					(String) searchHit.getSource().get("type"),
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
	public void index(SMDAbstractRepository repository, SMDDocument document) {

		if (logger.isDebugEnabled())
			logger.debug("index({})", document);

		try {

			String json = mapper.writeValueAsString(document);

			bulk.add(new IndexRequest(SMDINDEX, repository.type,
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
	public void delete(SMDAbstractRepository repository, String id) {

		if (logger.isDebugEnabled())
			logger.debug("delete({})", id);

		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException(
					"The id of document can't be null or empty");
		}

		try {
			bulk.add(new DeleteRequest(SMDINDEX, repository.url, id));
		} catch (Exception e) {
			logger.warn("Can not delete document {} of type  {}", id,
					repository.url);
			throw new RuntimeException("Can not delete document : " + id
					+ "whith type " + repository.url + ": "
					+ e.getMessage());
		}

		if (logger.isDebugEnabled())
			logger.debug("/delete()={}", id);

	}

	@Override
	public SMDSearchResponse searchFileByDirectory(
			SMDAbstractRepository smdAbstractPlugin, String directory, int first,
			int pageSize) {
		if (logger.isDebugEnabled())
			logger.debug("searchFileByDirectory('{}', {}, {})", directory,
					first, pageSize);

		SMDSearchResponse searchResponse = null;

//		BoolFilterBuilder filters = FilterBuilders.boolFilter().must(
//				FilterBuilders.termFilter("pathDirectory", directory));
//		BoolQueryBuilder qb = QueryBuilders.boolQuery();
//
//		QueryBuilder query = QueryBuilders.filteredQuery(qb, filters);

		
		QueryBuilder query = QueryBuilders.prefixQuery("pathDirectory", directory);
		
		org.elasticsearch.action.search.SearchResponse searchHits = esClient
				.prepareSearch().setIndices(SMDINDEX)
				.setTypes(smdAbstractPlugin.type).setQuery(query)
				.setFrom(first).setSize(pageSize).execute().actionGet();

		List<SMDResponseDocument> documents = new ArrayList<SMDResponseDocument>();
		for (SearchHit searchHit : searchHits.getHits()) {

			SMDResponseDocument smdResponseDocument = new SMDResponseDocument(
					(String) searchHit.getSource().get("type"),
					(String) searchHit.getSource().get("url"),
					(String) searchHit.getSource().get("contentType"), null);

			documents.add(smdResponseDocument);
		}

		searchResponse = new SMDSearchResponse(searchHits.getTookInMillis(),
				searchHits.getHits().totalHits(), documents);

		if (logger.isDebugEnabled())
			logger.debug("searchFileByDirectory('{}', {}, {})", directory,
					first, pageSize);
		return searchResponse;

	}


	@Override
	public List<SMDAbstractRepository> getRepositories() {
		try {
			org.elasticsearch.action.search.SearchResponse searchHits = esClient
					.prepareSearch(SMDADMIN).setTypes(SMDADMIN_REPOSITORIES)
					.execute().actionGet();

			if (searchHits.getHits().totalHits() == 0) {
				return null;
			}

			List<SMDAbstractRepository> plugins = new ArrayList<SMDAbstractRepository>();
			for (SearchHit searchHit : searchHits.getHits()) {
				plugins.add(mapper.readValue(
						searchHit.getSourceAsString(),
						PluginsUtils.getAll()
								.get(searchHit.getSource().get("type"))
								.getClass()));
			}

			return plugins;
		} catch (Exception e) {
			logger.error("Can not checkout the configuration.",e);
			throw new RuntimeException("Can not checkout the configuration.");
		}
	}

	@Override
	public void save(SMDAbstractRepository repository) {
		try {
			esClient.prepareIndex(SMDADMIN, SMDADMIN_REPOSITORIES)
					.setSource(mapper.writeValueAsString(repository)).execute()
					.actionGet();
		} catch (Exception e) {
			throw new RuntimeException("Can not save the configuration.");
		}
	}

	@Override
	public SMDAbstractRepository get(String id) {
		
		if(id==null){
			throw new IllegalArgumentException("you can't search a repository with null id");
		}
		
		try {
			GetResponse response = esClient
					.prepareGet(SMDADMIN, SMDADMIN_REPOSITORIES, id)
					.setOperationThreaded(false).execute().actionGet();

			if (!response.isExists())
				return null;

			return mapper.readValue(response.getSourceAsString(), PluginsUtils
					.getAll().get(response.getSource().get("type")).getClass());
		} catch (Exception e) {
			throw new RuntimeException("Can not save the configuration.",e);
		}

	}

}
