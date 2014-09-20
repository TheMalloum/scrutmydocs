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

import javassist.NotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.contract.SMDSettings;
import org.scrutmydocs.plugins.PluginsUtils;
import org.scrutmydocs.plugins.SMDAbstractPlugin;
import org.scrutmydocs.scan.ScanDocuments;
import org.scrutmydocs.search.SMDSearchFactory;
import org.scrutmydocs.settings.SMDSettingsFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping("/2/settings")
public class SettingsApi {
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Get settings
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
    SMDSettings get() throws Exception {
		return SMDSettingsFactory.getInstance().getSettings();
	}

	/**
	 * Put settings
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody
	void put(@RequestBody SMDSettings settings)
			throws Exception {
		
		
		// verification 
		HashMap<String, SMDAbstractPlugin> plugins = PluginsUtils.getAll();
		
		for (SMDAbstractPlugin setting : settings.smdAbstractPlugins) {
			if(plugins.get(setting.name()) == null){
				throw new NotFoundException(" the plugin "+ setting.name()+"dosn't exite");
			}
			
		}
		
        SMDSettingsFactory.getInstance().saveSettings(settings);
	}

	/**
	 * 
	 * force to Scan All repositories
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	void post() throws Exception {
		new ScanDocuments().scan();

	}
}
