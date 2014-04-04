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

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.contract.SMDsearch;
import org.scrutmydocs.datasource.SMDDataSource;
import org.scrutmydocs.webapp.api.settings.rivers.AbstractRiverHelper;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

class ESSearchService implements SMDsearch {

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	private SMDDataSource smdDataSource;

	final private static String SMDINDEX = "srutmydocs-docs";

	final private static String SMDADMIN = "srutmydocs-admin";

	private ObjectMapper mapper = new ObjectMapper();

	Client esClient;

	public ESSearchService() {
		// TODO Auto-generated constructor stub
	}

	public ESSearchService(SMDDataSource smdDataSource,Client client) {
		this.smdDataSource = smdDataSource;
		this.esClient= client;
//		mapper.configure(Feature., arg1)
	}

	@Override
	/**
	 *  TODO don't forget to excludes the configuration index.
	 */
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
				.prepareSearch().setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(qb).setFrom(first).setSize(pageSize)
				.addHighlightedField("name").addHighlightedField("file")
				.setHighlighterPreTags("<span class='badge badge-info'>")
				.setHighlighterPostTags("</span>").addFields("*", "_source")
				.execute().actionGet();

		totalHits = searchHits.getHits().totalHits();
		took = searchHits.getTookInMillis();

		List<SMDDocument> documents = new ArrayList<SMDDocument>();
		for (SearchHit searchHit : searchHits.getHits()) {

			String name = AbstractRiverHelper.getSingleStringValue("name",
					searchHit.getSource());

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

			SMDDocument smdDocument = new SMDDocument(searchHit.id(), name,
					null, null, null, highlights);

			documents.add(smdDocument);
		}

		searchResponse = new SMDSearchResponse(took, totalHits, documents);

		if (logger.isDebugEnabled())
			logger.debug("/google({}) : {}", search, totalHits);

		return searchResponse;

	}

	@Override
	public void index(SMDDocument document) {

		if (logger.isDebugEnabled())
			logger.debug("push({})", document);

		if (document == null || StringUtils.isEmpty(document.name == null)
				|| StringUtils.isEmpty(document.id == null)) {
			throw new IllegalArgumentException(
					"The document can't be null and must have name or id");
		}

		try {
			IndexResponse response = esClient
					.prepareIndex(SMDINDEX, smdDataSource.id, document.id)
					.setSource(
							jsonBuilder()
									.startObject()
									.field("name", document.name)
									.field("postDate", document.date)
									.startObject("file")
									.field("_content_type",
											document.contentType)
									.field("_name", document.name)
									.field("content", document.content)
									.endObject().endObject()).execute()
					.actionGet();

		} catch (Exception e) {
			logger.warn("Can not index document {}", document.name);
			throw new RuntimeException("Can not index document : "
					+ document.name + ": " + e.getMessage());
		}

		if (logger.isDebugEnabled())
			logger.debug("/push()={}", document);

	}

	@Override
	public void delete(String id) {

		if (logger.isDebugEnabled())
			logger.debug("push({})", id);

		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException(
					"The id of document can't be null or empty");
		}

		try {
			esClient.prepareDelete(SMDINDEX, smdDataSource.id, id).execute()
					.actionGet();

		} catch (Exception e) {
			logger.warn("Can not index document {} if type  {}", id,
					smdDataSource.id);
			throw new RuntimeException("Can not delete document : " + id
					+ "whith type " + smdDataSource.id + ": " + e.getMessage());
		}

		if (logger.isDebugEnabled())
			logger.debug("/delete()={}", id);

	}

	@Override
	public List<SMDDataSource> getConf() {

		List<SMDDataSource> response = new ArrayList<SMDDataSource>();
		try {

			org.elasticsearch.action.search.SearchResponse searchHits = esClient
					.prepareSearch(SMDADMIN)
					.setTypes(this.smdDataSource.name()).execute().actionGet();

			for (SearchHit searchHit : searchHits.getHits()) {


				response.add(mapper.readValue(searchHit.getSourceAsString(),
						this.smdDataSource.getClass()));

			}

		} catch (Exception e) {
			logger.error("Can not checkout the configuration's document : "
					+ this.smdDataSource.name() + "whith type "
					+ smdDataSource.id);
			throw new RuntimeException(
					"Can not checkout the configuration's document : "
							+ this.smdDataSource.name() + "whith type "
							+ smdDataSource.id + ": " + e);
		}

		return response;
	}

	@Override
	public void saveConf() {
		try {
			esClient.prepareIndex(SMDADMIN, this.smdDataSource.name(),
					this.smdDataSource.id)
					.setSource(mapper.writeValueAsString(this.smdDataSource))
					.execute().actionGet();
		} catch (Exception e) {
			throw new RuntimeException("Can not save the configuration : "
					+ this.smdDataSource.name() + "whith type "
					+ smdDataSource.id + ": " + e.getMessage());
		}

	}

}
