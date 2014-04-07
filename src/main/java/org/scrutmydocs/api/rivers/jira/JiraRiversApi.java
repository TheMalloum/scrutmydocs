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

package org.scrutmydocs.api.rivers.jira;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.api.Api;
import org.scrutmydocs.api.rivers.CommonRiversApi;
import org.scrutmydocs.api.rivers.SMDRestResponse;
import org.scrutmydocs.datasource.jira.JiraSMDDataSource;
import org.scrutmydocs.webapp.service.settings.rivers.RiverService;
import org.scrutmydocs.webapp.service.settings.rivers.jira.AdminJiraRiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/1/settings/rivers/jira")
public class JiraRiversApi extends CommonRiversApi {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	protected AdminJiraRiverService adminService;
	@Autowired
	protected RiverService riverService;

	@Override
	public Api[] helpApiList() {
		Api[] apis = new Api[7];
		apis[0] = new Api("/1/settings/rivers/jira", "GET",
				"Get all existing Jira rivers");
		apis[1] = new Api("/1/settings/rivers/jira/{name}", "GET",
				"Get details about a Jira river");
		apis[2] = new Api("/1/settings/rivers/jira", "PUT",
				"Create or update a Jira river");
		apis[3] = new Api("/1/settings/rivers/jira", "POST",
				"Create or update a Jira river");
		apis[4] = new Api("/1/settings/rivers/jira/{name}", "DELETE",
				"Delete an existing Jira river");
		apis[5] = new Api("/1/settings/rivers/jira/{name}/start", "GET",
				"Start a Jira river");
		apis[6] = new Api("/1/settings/rivers/jira/{name}/stop", "GET",
				"Stop a Jira river");
		return apis;
	}

	@Override
	public String helpMessage() {
		return "The /1/settings/rivers/jira API manage Jira rivers.";
	}

	/**
	 * Search for all Jira rivers
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	SMDRestResponse get() {
		return super.get(new JiraSMDDataSource());
	}

	/**
	 * Search for one Jira river
	 * 
	 * @return
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public @ResponseBody
	SMDRestResponse get(@PathVariable final String id) {

		return super.get(new JiraSMDDataSource(), id);

	}

	/**
	 * Create or Update a Jira river
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody
	SMDRestResponse put(@RequestBody JiraSMDDataSource jirariver) {

		return super.put(jirariver);
	}

	/**
	 * Create or Update a Jira river
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	SMDRestResponse push(@RequestBody JiraSMDDataSource jirariver) {
		return super.put(jirariver);
	}

	/**
	 * Remove an Jira river
	 * 
	 * @return
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	SMDRestResponse delete(@PathVariable final String id) {

		return super.delete(new JiraSMDDataSource(),id);

	}

	/**
	 * Start a Jira river
	 * 
	 * @return
	 */
	@RequestMapping(value = "{id}/start", method = RequestMethod.GET)
	public @ResponseBody
	SMDRestResponse start(@PathVariable final String id) {

		return super.start(id);
	}

	/**
	 * Stop a Jira river
	 * 
	 * @return
	 */
	@RequestMapping(value = "{id}/stop", method = RequestMethod.GET)
	public @ResponseBody
	SMDRestResponse stop(@PathVariable final String id) {
		return super.stop(id);
	}

}
