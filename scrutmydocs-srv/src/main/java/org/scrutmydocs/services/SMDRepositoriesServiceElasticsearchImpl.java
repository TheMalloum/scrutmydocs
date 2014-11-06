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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.search.SearchHit;
import org.scrutmydocs.domain.SMDRepository;
import org.scrutmydocs.dao.elasticsearch.ElasticsearchService;
import org.scrutmydocs.exceptions.SMDIllegalArgumentException;
import org.scrutmydocs.exceptions.SMDJsonParsingException;
import org.scrutmydocs.exceptions.SMDRepositoryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Component
public class SMDRepositoriesServiceElasticsearchImpl implements SMDRepositoriesService {

    private static final Logger logger = LoggerFactory.getLogger(SMDRepositoriesServiceElasticsearchImpl.class);

	final public static String SMDADMIN = "scrutmydocs-settings";
	final public static String SMDADMIN_REPOSITORIES = "repositories";

    private final SMDRepositoryReflectionService repositoryReflectionService;
    private final SMDDocumentService searchService;
    private final ElasticsearchService elasticsearchService;

	ObjectMapper mapper = new ObjectMapper();

    @Inject
	public SMDRepositoriesServiceElasticsearchImpl(SMDRepositoryReflectionService repositoryReflectionService,
                                                   SMDDocumentService searchService,
                                                   ElasticsearchService elasticsearchService) {
        this.searchService = searchService;
        this.repositoryReflectionService = repositoryReflectionService;
        this.elasticsearchService = elasticsearchService;

        // We could either create a transport client to connect on an existing cluster
        // Or run our own internal cluster
/*
        elasticsearchService.esClient().admin().cluster().prepareHealth().setWaitForYellowStatus().get();

        elasticsearchService.createIndex(SMDADMIN);
        elasticsearchService.pushMapping(SMDADMIN, SMDADMIN_REPOSITORIES);

		//TODO use ES methode to wait
		while (!elasticsearchService.esClient().admin().indices().prepareExists(SMDADMIN).get().isExists()) {
			try {
				Thread.sleep(1000 * 1);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
*/		// this.bulk = new BulkProcessor.Builder(esClient,
		// new BulkProcessor.Listener() {
		// @Override
		// public void beforeBulk(long l, BulkRequest bulkRequest) {
		// logger.debug("before bulking {} actions",
		// bulkRequest.numberOfActions());
		// }
		//
		// @Override
		// public void afterBulk(long l, BulkRequest bulkRequest,
		// BulkResponse bulkItemResponses) {
		// logger.debug(
		// "after bulking {} actions- has failure failures: {}",
		// bulkRequest.numberOfActions(),
		// bulkItemResponses.hasFailures());
		// // TODO check if errors
		// }
		//
		// @Override
		// public void afterBulk(long l, BulkRequest bulkRequest,
		// Throwable throwable) {
		// logger.warn("Error while executing bulk", throwable);
		// }
		// }).setFlushInterval(TimeValue.timeValueSeconds(5))
		// .setBulkActions(100).build();
	}

	@Override
	public void deleteRepositorySetting(SMDRepository repository) throws SMDIllegalArgumentException {

		if (logger.isDebugEnabled())
			logger.debug("delete({})", repository.id);

		if (repository.id == null || repository.id.isEmpty()) {
			throw new SMDIllegalArgumentException("The id of document can't be null or empty");
		}

		try {

            elasticsearchService.esClient().prepareDelete(SMDADMIN, SMDADMIN_REPOSITORIES, repository.id).get();
		} catch (Exception e) {
			logger.warn("Can not delete document {} of type  {}",
					repository.id, SMDADMIN_REPOSITORIES);
			throw new RuntimeException("Can not delete document : "
					+ repository.id + "with type " + repository.type + ": "
					+ e.getMessage());
		}

		if (logger.isDebugEnabled())
			logger.debug("/delete()={}", repository.id);

	}

	@Override
	public List<SMDRepository> getRepositories() {
		try {
            List<SMDRepository> plugins = new ArrayList<SMDRepository>();
			org.elasticsearch.action.search.SearchResponse searchHits = elasticsearchService.esClient()
					.prepareSearch(SMDADMIN).setTypes(SMDADMIN_REPOSITORIES)
					.execute().actionGet();

			if (searchHits.getHits().totalHits() == 0) {
				return plugins;
			}

			for (SearchHit searchHit : searchHits.getHits()) {
				plugins.add(mapper.readValue(searchHit.getSourceAsString(),
                        repositoryReflectionService.getListData((String) searchHit.getSource().get("type"))));
			}

			return plugins;
		} catch (Exception e) {
			logger.error("Can not checkout the configuration.", e);
			throw new RuntimeException("Can not checkout the configuration.");
		}
	}

	@Override
	public void save(SMDRepository repository) {
        assert repository != null;
        assert repository.id != null;

		try {
            elasticsearchService.esClient().prepareIndex(SMDADMIN, SMDADMIN_REPOSITORIES, repository.id)
					.setSource(mapper.writeValueAsString(repository))
                    .setRefresh(true)
                    .get();
		} catch (Exception e) {
			throw new RuntimeException("Can not save the configuration.");
		}
	}

	@Override
	public SMDRepository get(String id) throws SMDIllegalArgumentException, SMDJsonParsingException {

		if (id == null) {
			throw new SMDIllegalArgumentException(
					"you can't search a repository with null id");
		}

		try {
			GetResponse response = elasticsearchService.esClient()
					.prepareGet(SMDADMIN, SMDADMIN_REPOSITORIES, id)
					.setOperationThreaded(false).execute().actionGet();

			if (!response.isExists()) {
                throw new SMDRepositoryNotFoundException("repository [" + id + "] does not exist.");
            }

			return mapper.readValue(response.getSourceAsString(),
                    repositoryReflectionService.getListData((String) response.getSource().get("type")));
		} catch (Exception e) {
            // TODO Change to another exception
			throw new SMDJsonParsingException("Can not save the configuration.", e);
		}
	}

    public void deleteRepository(SMDRepository repository) throws SMDIllegalArgumentException {
        searchService.deleteDirectory(repository.url);
        deleteRepositorySetting(repository);
    }
}
