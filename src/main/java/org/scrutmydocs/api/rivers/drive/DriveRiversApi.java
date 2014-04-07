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

package org.scrutmydocs.api.rivers.drive;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.api.Api;
import org.scrutmydocs.api.rivers.CommonRiversApi;
import org.scrutmydocs.api.rivers.SMDRestResponse;
import org.scrutmydocs.datasource.drive.DriveSMDDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Rest API definitions for managing Google Drive rivers.
 * 
 * @author Laurent Broudoux
 */
@Controller
@RequestMapping("/1/settings/rivers/drive")
public class DriveRiversApi extends CommonRiversApi {

	/** Commons logger for diagnostic messages. */
	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	public String helpMessage() {
		return "The /1/settings/rivers/drive API manage Google Drive rivers.";
	}

	@Override
	public Api[] helpApiList() {
		Api[] apis = new Api[7];
		apis[0] = new Api("/1/settings/rivers/drive", "GET",
				"Get all existing Google Drive rivers");
		apis[1] = new Api("/1/settings/rivers/drive/{name}", "GET",
				"Get details about a Google Drive river");
		apis[2] = new Api("/1/settings/rivers/drive", "PUT",
				"Create or update a Google Drive river");
		apis[3] = new Api("/1/settings/rivers/drive", "POST",
				"Create or update a Google Drive river");
		apis[4] = new Api("/1/settings/rivers/drive/{name}", "DELETE",
				"Delete an existing Google Drive river");
		apis[5] = new Api("/1/settings/rivers/drive/{name}/start", "GET",
				"Start a Google Drive river");
		apis[6] = new Api("/1/settings/rivers/drive/{name}/stop", "GET",
				"Stop a Google Drive river");
		return apis;
	}

	/**
	 * Search for all Google Drive rivers.
	 * 
	 * @return Rest response containing list of available Drive rivers
	 * @throws Exception
	 *             if something goes wrong...
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	SMDRestResponse get() throws Exception {

		return super.get(new DriveSMDDataSource());
	}

	/**
	 * Serah for a specified Google Drive river.
	 * 
	 * @param id
	 *            Identifier of river to search for
	 * @return Rest response representing Drive river
	 * @throws Exception
	 *             if something goes wrong...
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public @ResponseBody
	SMDRestResponse get(@PathVariable final String id) throws Exception {

		return super.get(new DriveSMDDataSource(), id);
	}

	/**
	 * Create or Update a Google Drive river.
	 * 
	 * @return Rest response representing updated river.
	 * @throws Exception
	 *             if something goes wrong...
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody
	SMDRestResponse put(@RequestBody DriveSMDDataSource river) throws Exception {

		return super.put(river);
	}

	/**
	 * Create a Google Drive river.
	 * 
	 * @return Rest response representing updated river.
	 * @throws Exception
	 *             if something goes wrong...
	 */
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	SMDRestResponse post(@RequestBody DriveSMDDataSource river)
			throws Exception {
		return super.put(river);
	}

	/**
	 * Remove the specified Google Drive river.
	 * 
	 * @param id
	 *            Identifier of river to search for
	 * @return Rest response representing empty river
	 * @throws Exception
	 *             if something goes wrong...
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	SMDRestResponse delete(@PathVariable final String id) {

		return super.delete(id);
	}

	/**
	 * Start the specified Google Drive river.
	 * 
	 * @param id
	 *            Identifier of river to search for
	 * @return Rest response representing empty river
	 * @throws Exception
	 *             if something goes wrong...
	 */
	@RequestMapping(value = "{id}/start", method = RequestMethod.GET)
	public @ResponseBody
	SMDRestResponse start(@PathVariable final String id){
		return super.start(id);

	}

	/**
	 * Stop the specified Google Drive river.
	 * 
	 * @param id
	 *            Identifier of river to search for
	 * @return Rest response representing empty river
	 * @throws Exception
	 *             if something goes wrong...
	 */
	@RequestMapping(value = "{id}/stop", method = RequestMethod.GET)
	public @ResponseBody
	SMDRestResponse stop(@PathVariable final String id){
		return super.stop(id);
	}
}
