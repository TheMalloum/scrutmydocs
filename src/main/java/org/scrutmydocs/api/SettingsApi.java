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

package org.scrutmydocs.api;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.plugins.PluginsUtils;
import org.scrutmydocs.plugins.SMDAbstractPlugin;
import org.scrutmydocs.scan.ScanDocuments;
import org.scrutmydocs.search.SMDSettingsFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller()
@RequestMapping("/2/settings")
public class SettingsApi {
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Get settings
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value = "/all")
	public @ResponseBody List<SMDAbstractPlugin> getAll() throws Exception {
		return SMDSettingsFactory.getInstance().getSettings();
	}
	
	
	/**
	 * Get settings
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody SMDAbstractPlugin get(String id) throws Exception {
		return SMDSettingsFactory.getInstance().getSetting(id);
	}

	/**
	 * Put settings
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody void addScrutation(
			@RequestBody SMDAbstractPlugin setting) throws Exception {

		// verification
		HashMap<String, SMDAbstractPlugin> plugins = PluginsUtils.getAll();

		if (plugins.get(setting.name()) == null) {
			// todo find exception 404 in spring 4
			throw new IllegalArgumentException(" the plugin " + setting.name()
					+ "dosn't exite");
		}

		SMDSettingsFactory.getInstance().saveSetting(setting);
	}

	/**
	 * 
	 * force to Scan All repositories
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody void post() throws Exception {
		new ScanDocuments().scan();

	}
}
