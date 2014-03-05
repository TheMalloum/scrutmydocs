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
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.scrutmydocs.contract.SMDDataSource;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.contract.SMDHit;
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.contract.SMDsearch;
import org.scrutmydocs.webapp.api.settings.rivers.AbstractRiverHelper;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.springframework.beans.factory.annotation.Autowired;

class ESSearchService implements SMDsearch {

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	private SMDDataSource smdDataSource;
	
	@Autowired
	Client esClient;

	public ESSearchService(SMDDataSource smdDataSource) {
		this.smdDataSource = smdDataSource;
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

		List<SMDHit> hits = new ArrayList<SMDHit>();
		for (SearchHit searchHit : searchHits.getHits()) {
			SMDHit hit = new SMDHit();

			hit.setIndex(searchHit.getIndex());
			hit.setType(searchHit.getType());
			hit.setId(searchHit.getId());
			if (searchHit.getType().equals(SMDSearchProperties.INDEX_TYPE_DOC)) {
				if (searchHit.getSource() != null) {
					hit.setTitle(AbstractRiverHelper.getSingleStringValue(
							SMDSearchProperties.DOC_FIELD_NAME,
							searchHit.getSource()));

					if (searchHit.getSource() != null) {
						hit.setTitle(AbstractRiverHelper.getSingleStringValue(
								SMDSearchProperties.DOC_FIELD_NAME,
								searchHit.getSource()));
					}
					if (searchHit.getFields() != null
							&& searchHit.getFields().get("file.content_type") != null) {
						hit.setContentType((String) searchHit.getFields()
								.get("file.content_type").getValue());
					}
					if (searchHit.getHighlightFields() != null) {
						for (HighlightField highlightField : searchHit
								.getHighlightFields().values()) {

							Text[] fragmentsBuilder = highlightField
									.getFragments();

							for (Text fragment : fragmentsBuilder) {
								hit.getHighlights().add(fragment.string());
							}
						}
					}
				}
			}

			if (searchHit.getType().equals("jira_issue")) {
				if (searchHit.getSource() != null) {
					hit.setTitle("Issue "
							+ AbstractRiverHelper.getSingleStringValue(
									"issue_key", searchHit.getSource())
							+ " from project "
							+ AbstractRiverHelper.getSingleStringValue(
									"project_name", searchHit.getSource()));
					hit.setContentType(AbstractRiverHelper
							.getSingleStringValue("document_url",
									searchHit.getSource()));
				}
			}
			hits.add(hit);
		}

		searchResponse = new SMDSearchResponse(took, totalHits, hits);

		if (logger.isDebugEnabled())
			logger.debug("/google({}) : {}", search, totalHits);

		return searchResponse;

	}

	@Override
	public void index(SMDDocument smdDocument) {

	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveAdmin(Map<String, String> json) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, String> getAdmin() {
		// TODO Auto-generated method stub
		return null;
	}

}
