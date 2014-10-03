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

package org.scrutmydocs.repositories;

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
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.contract.SMDFileDocument;
import org.scrutmydocs.contract.SMDRepositoriesService;
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.contract.SMDSearchService;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticRepositoryImpl implements 
		SMDRepositoriesService {

	protected Logger logger = LogManager.getLogger();

	final public static String SMDADMIN = "scrutmydocs-admin";
	final public static String SMDADMIN_REPOSITORIES = "repositories";

	
	private Client esClient;

	private BulkProcessor bulk;

	ObjectMapper mapper = new ObjectMapper();

	public ElasticRepositoryImpl() {
		esClient = NodeBuilder.nodeBuilder().node().client();
		esClient.admin().cluster().prepareHealth().setWaitForYellowStatus()
				.execute().actionGet();

		createIndex(SMDADMIN);


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

	public void createIndex(String index) {
		if (logger.isDebugEnabled())
			logger.debug("createIndex({}, {}, {})", index);

		if (!esClient.admin().indices().prepareExists(index).execute()
				.actionGet().isExists()) {
			esClient.admin().indices().prepareCreate(index).execute();
		}
	}


	@Override
	public void deleteRepository(SMDRepositoryData repository) {

		if (logger.isDebugEnabled())
			logger.debug("delete({})", repository.id);

		if (repository.id == null || repository.id.isEmpty()) {
			throw new IllegalArgumentException(
					"The id of document can't be null or empty");
		}

		try {
			bulk.add(new DeleteRequest(SMDADMIN, repository.type,repository.id));
		} catch (Exception e) {
			logger.warn("Can not delete document {} of type  {}", repository.id,
					repository.type);
			throw new RuntimeException("Can not delete document : " + repository.id
					+ "whith type " + repository.type + ": " + e.getMessage());
		}

		if (logger.isDebugEnabled())
			logger.debug("/delete()={}", repository.id);

	}
	
	
	@Override
	public List<SMDRepositoryData> getRepositories() {
		try {
			org.elasticsearch.action.search.SearchResponse searchHits = esClient
					.prepareSearch(SMDADMIN).setTypes(SMDADMIN_REPOSITORIES)
					.execute().actionGet();

			if (searchHits.getHits().totalHits() == 0) {
				return null;
			}

			List<SMDRepositoryData> plugins = new ArrayList<SMDRepositoryData>();
			for (SearchHit searchHit : searchHits.getHits()) {
				plugins.add(mapper.readValue(
						searchHit.getSourceAsString(),
						SMDRepositoriesFactory.getAllTypeRepositories().get(
								searchHit.getSource().get("type"))));
			}

			return plugins;
		} catch (Exception e) {
			logger.error("Can not checkout the configuration.", e);
			throw new RuntimeException("Can not checkout the configuration.");
		}
	}

	@Override
	public void save(SMDRepositoryData repository) {
		try {
			esClient.prepareIndex(SMDADMIN, SMDADMIN_REPOSITORIES,repository.id)
					.setSource(mapper.writeValueAsString(repository)).execute()
					.actionGet();
		} catch (Exception e) {
			throw new RuntimeException("Can not save the configuration.");
		}
	}

	@Override
	public SMDRepositoryData get(String id) {

		if (id == null) {
			throw new IllegalArgumentException(
					"you can't search a repository with null id");
		}

		try {
			GetResponse response = esClient
					.prepareGet(SMDADMIN, SMDADMIN_REPOSITORIES, id)
					.setOperationThreaded(false).execute().actionGet();

			if (!response.isExists())
				return null;

			return mapper.readValue(
					response.getSourceAsString(),
					SMDRepositoriesFactory.getAllTypeRepositories().get(
							response.getSource().get("type")));
		} catch (Exception e) {
			throw new RuntimeException("Can not save the configuration.", e);
		}

	}

}
