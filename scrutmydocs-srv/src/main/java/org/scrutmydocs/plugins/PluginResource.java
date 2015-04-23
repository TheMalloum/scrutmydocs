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

import java.util.Collection;

import javax.inject.Inject;

import org.scrutmydocs.resources.ScrutmydocsApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import restx.annotations.GET;
import restx.annotations.RestxResource;
import restx.factory.AutoStartable;
import restx.factory.Component;
import restx.security.PermitAll;

import com.google.common.base.Optional;

@Component
@RestxResource(ScrutmydocsApi.API_ROOT_REPOSITORY)
public class PluginResource implements AutoStartable {
    private static final Logger logger = LoggerFactory.getLogger(PluginResource.class);

    private final PluginService pluginService;

    @Inject
    public PluginResource(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    public void start() {
        logger.debug("starting PluginResource - loading existing repositories");
        pluginService.registerPlugins();
    }

    /**
     * Get repositories. You can filter by type using ?type=fs
     */
    @GET("/")
    @PermitAll
	public Collection<String> getRepositories(Optional<String> type) {
        logger.debug("getRepositories({})", type);

        return pluginService.getPlugins().keySet();
	}

	

   
}
