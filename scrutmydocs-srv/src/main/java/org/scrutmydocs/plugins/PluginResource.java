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

package org.scrutmydocs.plugins;

import com.google.common.base.Optional;
import org.scrutmydocs.domain.SMDRepository;
import org.scrutmydocs.exceptions.SMDIllegalArgumentException;
import org.scrutmydocs.exceptions.SMDJsonParsingException;
import org.scrutmydocs.resources.ScrutmydocsApi;
import org.scrutmydocs.services.SMDRepositoriesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.annotations.DELETE;
import restx.annotations.GET;
import restx.annotations.POST;
import restx.annotations.RestxResource;
import restx.factory.AutoStartable;
import restx.factory.Component;
import restx.security.PermitAll;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RestxResource(ScrutmydocsApi.API_ROOT_REPOSITORY)
public class PluginResource implements AutoStartable {
    private static final Logger logger = LoggerFactory.getLogger(PluginResource.class);

    private final SMDRepositoriesService repositoriesService;
    private final PluginService pluginService;
    private List<SMDRepository> repositories;

    @Inject
    public PluginResource(SMDRepositoriesService repositoriesService,
                          PluginService pluginService) {
        this.repositoriesService = repositoriesService;
        this.pluginService = pluginService;
    }

    @Override
    public void start() {
        logger.debug("starting PluginResource - loading existing repositories");
        repositories = repositoriesService.getRepositories();
        pluginService.registerPlugins();
    }

    /**
     * Get repositories. You can filter by type using ?type=fs
     */
    @GET("/")
    @PermitAll
	public List<SMDRepository> getRepositories(Optional<String> type) {
        logger.debug("getRepositories({})", type);

		List<SMDRepository> allRepositories = repositoriesService.getRepositories();

        if (!type.isPresent()) {
            return allRepositories;
        }

        List<SMDRepository> filteredRepositories = new ArrayList<>();
        for (SMDRepository repository : allRepositories) {
			if (repository.type.equals(type.get())) {
				filteredRepositories.add(repository);
			}
		}

		return filteredRepositories;
	}

	/**
	 * Get repository
	 */
    @GET("/{id}")
    @PermitAll
	public SMDRepository getRepository(String id) throws IOException {
        logger.debug("getRepository({})", id);
        try {
            return repositoriesService.get(id);
        } catch (SMDIllegalArgumentException | SMDJsonParsingException e) {
            // TODO Remove when Restx will be fixed - see https://github.com/restx/restx/issues/121
            throw new IOException(e);
        }
    }

	/**
	 * DELETE repository
	 */
    @DELETE("/{id}")
    @PermitAll
	public void delete(String id) throws IOException {
        try {
            repositoriesService.deleteRepository(repositoriesService.get(id));
        } catch (SMDIllegalArgumentException | SMDJsonParsingException e) {
            // TODO Remove when Restx will be fixed - see https://github.com/restx/restx/issues/121
            throw new IOException(e);
        }
    }

	/**
	 * add or update a repository
	 */
    @POST("/")
    @PermitAll
	public SMDRepository addRepository(SMDRepository repository) {

		if (repository.id == null || repository.id.trim().isEmpty()) {
			repository.id = UUID.randomUUID().toString();
		} else {
            try {
                delete(repository.id);
            } catch (IOException e) {
                // We ignore it as we are probably trying to create a new repository
                logger.debug("repository [{}] not found. Skipping deletion.", repository.id);
            }
		}

        repositoriesService.save(repository);
		
		return repository;
	}

    /*
        @POST("/_start")
        @PermitAll
    */
	public void start(String id) throws SMDJsonParsingException, SMDIllegalArgumentException {
		SMDRepository setting = repositoriesService.get(id);
		setting.start();
        repositoriesService.save(setting);
	}

    /*
        @POST("/_stop")
        @PermitAll
    */
	public void stop(String id) throws SMDJsonParsingException, SMDIllegalArgumentException {
		SMDRepository setting = repositoriesService.get(id);
		setting.stop();
        repositoriesService.save(setting);
	}
}
