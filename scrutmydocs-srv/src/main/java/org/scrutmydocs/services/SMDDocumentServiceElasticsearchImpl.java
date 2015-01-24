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

package org.scrutmydocs.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.scrutmydocs.converters.JsonToSMDDocumentService;
import org.scrutmydocs.dao.elasticsearch.ElasticsearchService;
import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.domain.SMDResponseDocument;
import org.scrutmydocs.domain.SMDSearchQuery;
import org.scrutmydocs.domain.SMDSearchResponse;
import org.scrutmydocs.exceptions.SMDDocumentNotFoundException;
import org.scrutmydocs.exceptions.SMDIndexException;
import org.scrutmydocs.exceptions.SMDJsonParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.FilterBuilders.matchAllFilter;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.simpleQueryString;

@Component
public class SMDDocumentServiceElasticsearchImpl implements SMDDocumentService {

    private static final Logger logger = LoggerFactory.getLogger(SMDDocumentServiceElasticsearchImpl.class);

	final public static String SMDINDEX = "scrutmydocs-docs";
	final public static String SMDTYPE = "docs";

	private BulkProcessor bulk;

    private final ElasticsearchService elasticsearchService;
    private final JsonToSMDDocumentService jsonToSMDDocumentService;

    // TODO Inject one single instance for the full project
	ObjectMapper mapper = new ObjectMapper();

    @Inject
	public SMDDocumentServiceElasticsearchImpl(ElasticsearchService elasticsearchService,
                                               JsonToSMDDocumentService jsonToSMDDocumentService) {
		logger.debug("Starting SMDDocumentServiceElasticsearchImpl");
        this.elasticsearchService = elasticsearchService;
        this.jsonToSMDDocumentService = jsonToSMDDocumentService;

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		this.bulk = new BulkProcessor.Builder(elasticsearchService.esClient(),
				new BulkProcessor.Listener() {
					@Override
					public void beforeBulk(long bulkId, BulkRequest bulkRequest) {
						logger.debug("before bulking [{}] actions, bulkId [{}]",
								bulkRequest.numberOfActions(),
                                bulkId);
					}

					@Override
					public void afterBulk(long bulkId, BulkRequest bulkRequest,
							BulkResponse bulkItemResponses) {
						logger.debug("after bulkId [{}] with [{}] actions - has failures? : [{}]",
                                bulkId,
                                bulkRequest.numberOfActions(),
								bulkItemResponses.hasFailures());
						// TODO check if errors ?
					}

					@Override
					public void afterBulk(long bulkId, BulkRequest bulkRequest,
							Throwable throwable) {
						logger.warn("Error while executing bulk [" + bulkId + "]", throwable);
					}
				}).setFlushInterval(TimeValue.timeValueSeconds(5))
				.setBulkActions(100).build();

		logger.debug("Started SMDDocumentServiceElasticsearchImpl");
	}

	@Override
	public SMDDocument getDocument(String id) throws SMDDocumentNotFoundException, SMDJsonParsingException {
		GetResponse response = elasticsearchService.esClient().prepareGet(SMDINDEX, SMDTYPE, id).get();

		if (!response.isExists()) {
            throw new SMDDocumentNotFoundException();
        }

        return jsonToSMDDocumentService.toDocument(response.getSourceAsString());
	}

    @Override
    public void deleteDocument(String id) {
        bulk.add(new DeleteRequest(SMDINDEX, SMDTYPE, id));
    }

    @Override
	public SMDSearchResponse search(SMDSearchQuery searchQuery) throws SMDJsonParsingException {
        logger.debug("search({})", searchQuery);

		SMDSearchResponse searchResponse;

		QueryBuilder query;
		if (!Strings.hasText(searchQuery.search)) {
			query = matchAllQuery();
		} else {
			FilterBuilder filter = matchAllFilter();
			QueryBuilder qb = simpleQueryString(searchQuery.search)
                    .field("content")
                    .field("meta.author", 1.5f)
                    .field("meta.keywords", 2.0f)
                    .field("meta.title", 3.0f)
                    .field("file.filename", 1.0f);
            query = QueryBuilders.filteredQuery(qb, filter);
		}

		SearchRequestBuilder request = elasticsearchService.esClient()
				.prepareSearch().setIndices(SMDINDEX).setTypes(SMDTYPE)
				.setQuery(query)
				.setFrom(searchQuery.first)
                .setSize(searchQuery.pageSize)
				.addHighlightedField("content")
				.addHighlightedField("meta.author")
				.addHighlightedField("meta.keywords")
				.addHighlightedField("meta.title")
				.addHighlightedField("file.filename")
				.setHighlighterPreTags("<span class='badge badge-info'>")
				.setHighlighterPostTags("</span>")

				.addFields("*", "_source");
		logger.trace("search: [{}]", request.toString());
		SearchResponse hits = request.get();

        logger.trace("result: {}", hits.toString());

		List<SMDResponseDocument> documents = new ArrayList<>();
		for (SearchHit hit : hits.getHits()) {
			ImmutableList.Builder<String> highlights = ImmutableList.builder();
			SMDDocument smdDocument = jsonToSMDDocumentService.toDocument(hit.getSourceAsString());
			for (HighlightField highlightField : hit.getHighlightFields().values()) {
				for (Text fragment : highlightField.getFragments()) {
					highlights.add(fragment.string());
				}
			}
			SMDResponseDocument responseDocument = new SMDResponseDocument(
					highlights.build(), smdDocument);
			documents.add(responseDocument);
		}

		searchResponse = new SMDSearchResponse(
                hits.getTookInMillis(),
                hits.getHits().totalHits(),
                documents);

        logger.debug("/google({}) : {}", searchQuery, hits.getHits().totalHits());

		return searchResponse;

	}

	@Override
	public void index(SMDDocument document) throws SMDIndexException {
        logger.debug("indexing document [{}]/[{}]", document.type, document.id);
        logger.trace("index({})", document);

		try {
			String json = mapper.writeValueAsString(document);
            logger.trace("json generated({})", json);
			bulk.add(new IndexRequest(SMDINDEX, SMDTYPE, document.id).source(json));
		} catch (Exception e) {
			logger.warn("Can not index document {}", document.file.filename);
			throw new SMDIndexException("Can not index document : "
					+ document.file.filename + ": " + e.getMessage());
		}

        logger.debug("document [{}]/[{}] indexed", document.type, document.id);
	}

	@Override
	public void deleteDirectory(String directory) {
        logger.debug("deleteAllDocumentsInDirectory('directory : {}', type : {})", directory, SMDTYPE);

		FilterBuilder filter = FilterBuilders.prefixFilter("pathDirectory", directory);
		QueryBuilder query = QueryBuilders.filteredQuery(matchAllQuery(), filter);

        elasticsearchService.esClient().prepareDeleteByQuery(SMDINDEX).setTypes(SMDTYPE)
				.setQuery(query).get();

	}
}
