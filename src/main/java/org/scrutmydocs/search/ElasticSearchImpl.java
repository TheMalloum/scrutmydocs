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

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.contract.SMDFileDocument;
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.contract.SMDSearchService;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticSearchImpl implements SMDSearchService {

	protected Logger logger = LogManager.getLogger();

	final public static String SMDINDEX = "scrutmydocs-docs";
	final public static String SMDTYPE = "docs";

	private Client esClient;

	private BulkProcessor bulk;

	ObjectMapper mapper = new ObjectMapper();

	public ElasticSearchImpl() {
		esClient = NodeBuilder.nodeBuilder().node().client();
		esClient.admin().cluster().prepareHealth().setWaitForYellowStatus()
				.execute().actionGet();

		createIndex();

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
						logger.debug(
								"after bulking {} actions- has failure failures: {}",
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
	public SMDFileDocument getDocument(String id) {

		GetResponse response = esClient.prepareGet(SMDINDEX, SMDTYPE, id)
				.execute().actionGet();

		if (!response.isExists())
			return null;
		SMDFileDocument smdFileDocument = null;
		try {
			smdFileDocument = new SMDFileDocument((String) response.getSource()
					.get("id"), (String) response.getSource().get("name"),
					(String) response.getSource().get("url"), (String) response
							.getSource().get("contentType"), (String) response
							.getSource().get("type"), null, (String) response
							.getSource().get("pathDirectory"),
					(String) response.getSource().get("content"), null);
			// @TODO add Date

		} catch (Exception e) {
			logger.warn("Can not fetch document {}", id);
			throw new RuntimeException("Can not index fetch document : " + id
					+ ": " + e.getMessage());
		}

		return smdFileDocument;
	}

	@Override
	public SMDSearchResponse search(String search, int first, int pageSize) {
		if (logger.isDebugEnabled())
			logger.debug("google('{}', {}, {})", search, first, pageSize);

		long totalHits = -1;
		long took = -1;

		SMDSearchResponse searchResponse = null;

		QueryBuilder qb;
		if (search == "*") {
			qb = matchAllQuery();
		} else {
			qb = queryString(search);
		}

		org.elasticsearch.action.search.SearchResponse searchHits = esClient
				.prepareSearch().setIndices(SMDINDEX)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb)
				.setFrom(first).setSize(pageSize)
				.addHighlightedField("file.filename")
				.addHighlightedField("content")
				.addHighlightedField("meta.title")
				.setHighlighterPreTags("<span class='badge badge-info'>")
				.setHighlighterPostTags("</span>").addFields("*", "_source")
				.execute().actionGet();
		
		totalHits = searchHits.getHits().totalHits();
		took = searchHits.getTookInMillis();

		List<SMDDocument> documents = new ArrayList<SMDDocument>();
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

			SMDDocument smdResponseDocument = new SMDDocument(
					(String) searchHit.getSource().get("id"),
					(String) searchHit.getSource().get("name"),
					(String) searchHit.getSource().get("url"),
					(String) searchHit.getSource().get("contentType"),
					(String) searchHit.getSource().get("type"),
					(String) searchHit.getSource().get("pathDirectory"),
					highlights);

			documents.add(smdResponseDocument);
		}

		searchResponse = new SMDSearchResponse(took, totalHits, documents);

		if (logger.isDebugEnabled())
			logger.debug("/google({}) : {}", search, totalHits);

		return searchResponse;

	}

	@Override
	public void index(SMDFileDocument document) {

		if (logger.isDebugEnabled())
			logger.debug("index({})", document);

		try {

			String json = mapper.writeValueAsString(document);

			bulk.add(new IndexRequest(SMDINDEX, SMDTYPE, document.id)
					.source(json));
		} catch (Exception e) {
			logger.warn("Can not index document {}", document.name);
			throw new RuntimeException("Can not index document : "
					+ document.name + ": " + e.getMessage());
		}

		if (logger.isDebugEnabled())
			logger.debug("/index()={}", document);

	}

	@Override
	public void deleteDirectory(String directory) {
		if (logger.isDebugEnabled())
			logger.debug(
					"deleteAllDocumentsInDirectory('directory : {}', type : {})",
					directory, SMDTYPE);

		BoolFilterBuilder filters = FilterBuilders.boolFilter().must(
				FilterBuilders.prefixFilter("pathDirectory", directory));

		MatchAllQueryBuilder qb = QueryBuilders.matchAllQuery();

		QueryBuilder query = QueryBuilders.filteredQuery(qb, filters);

		esClient.prepareSearch(SMDINDEX).setTypes(SMDTYPE).setQuery(query)
				.get();

		esClient.prepareDeleteByQuery(SMDINDEX).setTypes(SMDTYPE)
				.setQuery(query).get();

	}

	public void createIndex() {
		if (logger.isDebugEnabled())
			logger.debug("createIndex({}, {}, {})", SMDINDEX);

		if (!esClient.admin().indices().prepareExists(SMDINDEX).execute()
				.actionGet().isExists()) {
			esClient.admin().indices().prepareCreate(SMDINDEX).execute();

		}

		try {
			pushMapping();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void pushMapping() throws Exception {
		if (logger.isTraceEnabled())
			logger.trace("pushMapping(" + SMDINDEX + "," + SMDTYPE + "," + ")");

		if (isMappingExist()) {
			logger.info("Mapping definition for [" + SMDINDEX + "]/[" + SMDTYPE
					+ "].");
			return;
		}

		// Read the mapping json file if exists and use it
		String source = IOUtils.toString(ElasticSearchImpl.class
				.getClassLoader().getResourceAsStream(SMDTYPE + ".json"));

		if (source != null) {
			if (logger.isTraceEnabled())
				logger.trace("Mapping for [" + SMDINDEX + "]/[" + SMDTYPE
						+ "]=" + source);
			// Create type and mapping
			PutMappingResponse response = esClient.admin().indices()
					.preparePutMapping(SMDINDEX).setType(SMDTYPE)
					.setSource(source).execute().actionGet();
			if (!response.isAcknowledged()) {
				logger.fatal("the mapping {} isn't push ", SMDTYPE);
				throw new Exception("the mapping " + SMDTYPE + " isn't push ");

			}
		} else {
			throw new Exception("Could not finde mapping for type for type "
					+ SMDTYPE);
		}

	}

	/**
	 * Check if a mapping already exists in an index
	 * 
	 * @param index
	 *            Index name
	 * @param type
	 *            Mapping name
	 * @return true if mapping exists
	 */
	private boolean isMappingExist() {
		IndexMetaData imd = null;
		ClusterState cs = esClient.admin().cluster().prepareState()
				.setIndices(SMDINDEX).execute().actionGet().getState();
		imd = cs.getMetaData().index(SMDINDEX);

		if (imd == null)
			return false;

		MappingMetaData mdd = imd.mapping(SMDTYPE);

		if (mdd != null)
			return true;
		return false;
	}
}
