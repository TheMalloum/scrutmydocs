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

package org.scrutmydocs.api.rivers.fs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.api.Api;
import org.scrutmydocs.api.rivers.CommonRiversApi;
import org.scrutmydocs.api.rivers.SMDRestResponse;
import org.scrutmydocs.datasource.fs.FSSMDDataSource;
import org.scrutmydocs.webapp.service.settings.rivers.RiverService;
import org.scrutmydocs.webapp.service.settings.rivers.fs.AdminFSRiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.scrutmydocs.webapp.api.settings.rivers.basic.data.BasicRiver;

@Controller
@RequestMapping("/2/settings/rivers/fs")
public class FSRiversApi extends CommonRiversApi {
	protected final Log logger = LogFactory.getLog(getClass());


	@Override
	public Api[] helpApiList() {
		Api[] apis = new Api[7];
		apis[0] = new Api("/2/settings/rivers/fs", "GET",
				"Get all existing FileSystem rivers");
		apis[1] = new Api("/2/settings/rivers/fs{name}", "GET",
				"Get details about a FileSystem river");
		apis[2] = new Api("/2/settings/rivers/fs", "PUT",
				"Create or update a FileSystem river");
		apis[3] = new Api("/2/settings/rivers/fs", "POST",
				"Create or update a FileSystem river");
		apis[4] = new Api("/2/settings/rivers/fs/{name}", "DELETE",
				"Delete an existing FileSystem river");
		apis[5] = new Api("/2/settings/rivers/fs/{name}/start", "GET",
				"Start a river");
		apis[6] = new Api("/2/settings/rivers/fs/{name}/stop", "GET",
				"Stop a river");
		return apis;
	}

	@Override
	public String helpMessage() {
		return "The /2/settings/rivers/fs API manage FileSystem rivers.";
	}

	/**
	 * Search for all FS rivers
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	SMDRestResponse get() throws Exception {

		return super.get(new FSSMDDataSource());
	}

	/**
	 * Search for one FS river
	 * 
	 * @return
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public @ResponseBody
	SMDRestResponse get(@PathVariable final String id) throws Exception {
		return super.get(new FSSMDDataSource(), id);
	}

	/**
	 * Create or Update a FS river
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody
	SMDRestResponse put(@RequestBody FSSMDDataSource fsriver) throws Exception {
		return super.put(fsriver);

	}

	/**
	 * Create or Update a FS river
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	SMDRestResponse push(@RequestBody FSSMDDataSource fsriver) throws Exception {
		return put(fsriver);
	}

	/**
	 * Remove an FS river
	 * 
	 * @return
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	SMDRestResponse delete(@PathVariable final String id) {

		return super.delete(new FSSMDDataSource(),id);
	}

	/**
	 * Start a river
	 * 
	 * @return
	 */
	@RequestMapping(value = "{id}/start", method = RequestMethod.GET)
	public @ResponseBody
	SMDRestResponse start(@PathVariable final String id) {

		return super.start(new FSSMDDataSource(),id);
	}

	/**
	 * Stop a river
	 * 
	 * @return
	 */
	@RequestMapping(value = "{id}/stop", method = RequestMethod.GET)
	public @ResponseBody
	SMDRestResponse stop(@PathVariable final String id) {
		return super.stop(new FSSMDDataSource(),id);
	}

}
