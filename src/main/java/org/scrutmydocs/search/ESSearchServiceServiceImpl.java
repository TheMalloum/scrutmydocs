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
import java.util.Map;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.scrutmydocs.contract.*;
import org.scrutmydocs.plugins.SMDAbstractPlugin;
import org.scrutmydocs.elasticsearch.SMDElasticsearchClientFactory;
import org.springframework.util.StringUtils;

class ESSearchServiceServiceImpl implements SMDSearchService {

    private ESLogger logger = Loggers.getLogger(ESSearchServiceServiceImpl.class);

	final public static String SMDINDEX = "scrutmydocs-docs";

	private Client esClient;

    private BulkProcessor bulk;

	public ESSearchServiceServiceImpl() {

		this.esClient = SMDElasticsearchClientFactory.getInstance();
        SMDElasticsearchClientFactory.createIndex(SMDINDEX);

        this.bulk = new BulkProcessor.Builder(esClient, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {
                logger.debug("before bulking {} actions", bulkRequest.numberOfActions());
            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkItemResponses) {
                logger.debug("after bulking {} actions: failures: {}", bulkRequest.numberOfActions(), bulkItemResponses.hasFailures());
                // TODO check if errors
            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
                logger.warn("Error while executing bulk", throwable);
            }
        })
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setBulkActions(100)
                .build();
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

			String name = getSingleStringValue("name", searchHit.getSource());

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
					null, null, null);
            SMDResponseDocument smdResponseDocument = new SMDResponseDocument(smdDocument, highlights);
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

		if (document == null || StringUtils.isEmpty(document.name == null)
				|| StringUtils.isEmpty(document.url == null)) {
			throw new IllegalArgumentException(
					"The document can't be null and must have name or id");
		}

        try {
            bulk.add(new IndexRequest(SMDINDEX, smdAbstractPlugin.url, document.url)
                        .source(
                                jsonBuilder()
                                        .startObject()
                                        .field("name", document.name)
                                        .field("postDate", document.date)
                                        .startObject("file")
                                        .field("_content_type",
                                                document.contentType)
                                        .field("_name", document.name)
                                        .field("content", document.content)
                                        .endObject().endObject()
                        ));
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

		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException(
					"The id of document can't be null or empty");
		}

		try {
            bulk.add(new DeleteRequest(SMDINDEX, smdAbstractPlugin.url, id));
		} catch (Exception e) {
			logger.warn("Can not delete document {} of type  {}", id,
					smdAbstractPlugin.url);
			throw new RuntimeException("Can not delete document : " + id
					+ "whith type " + smdAbstractPlugin.url + ": " + e.getMessage());
		}

		if (logger.isDebugEnabled())
			logger.debug("/delete()={}", id);

	}



	private static String getSingleStringValue(String path,
			Map<String, Object> content) {
		List<Object> obj = XContentMapValues.extractRawValues(path, content);
		if (obj.isEmpty())
			return null;
		else
			return ((String) obj.get(0));
	}

	@Override
	public SMDSearchResponse searchFileByDirectory(String directory, int first,
			int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}
}
