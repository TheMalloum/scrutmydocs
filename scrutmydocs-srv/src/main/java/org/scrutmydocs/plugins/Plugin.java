/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import org.scrutmydocs.services.SMDDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * You can extend this class if you want to provide plugin which will generate
 * documents from your data source
 */
public abstract class Plugin {
	protected static final Logger logger = LoggerFactory
			.getLogger(Plugin.class);

	protected final SMDDocumentService documentService;
	
	public Plugin(SMDDocumentService documentService) {
		this.documentService = documentService;
	}
	
	private boolean started = false;


	/**
	 * Supported type
	 */
	public abstract String type();

	/**
	 * Plugin name
	 */
	public abstract String name();

	/**
	 * Plugin version
	 */
	public abstract String version();
	
	
	 public abstract void scrut() ;
	 

	/**
	 * Start the plugin
	 */
	public void start() {
		logger.debug("starting plugin [{}]/[{}]", name(), version());

		started = true;
	}

	/**
	 * Stop the plugin
	 */
	public void stop() {
		logger.debug("stopping plugin [{}]/[{}]", name(), version());
		started = false;
	}

	public boolean isStarted() {
		return started;
	}

}
