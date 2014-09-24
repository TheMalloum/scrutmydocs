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

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.contract.SMDResponseDocument;
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.repositories.PluginsUtils;
import org.scrutmydocs.repositories.SMDAbstractRepository;
import org.scrutmydocs.repositories.SMDRepositoriesFactory;
import org.scrutmydocs.scan.ScanDocuments;
import org.scrutmydocs.search.SMDSearchFactory;

@Path("/2/settings")
public class RepositoriesApi {
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Get all repositoy scan bu scrutmydocs
	 * 
	 * @return
	 */
	@Path("/_all")
	@GET
	public Response getAll() throws Exception {
		return Response.ok(SMDRepositoriesFactory.getInstance().getRepositories())
				.build();
	}

	/**
	 * Get settings
	 * 
	 * @return
	 */
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") String id) throws Exception {

		return Response.ok(getSettings(id)).build();
	}

	/**
	 * DELETE settings
	 * 
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") String id) throws Exception {
		
		SMDAbstractRepository plugin = getSettings(id);

		int first = 0;
		int page = 100;
		long total = 1;
		while (first < total) {

			SMDSearchResponse searchResponse = SMDSearchFactory.getInstance()
					.searchFileByDirectory(plugin,plugin.url, first, page);
			for (SMDResponseDocument smdResponseDocument : searchResponse.smdDocuments) {
				logger.debug("remove file " + smdResponseDocument.url + " ....");
				SMDSearchFactory.getInstance().delete(plugin,
						smdResponseDocument.url);
			}

			total = searchResponse.totalHits;
			first = +page;
		}

	}

	/**
	 * Put settings
	 * 
	 * @return
	 */

	@PUT
	public void put(SMDAbstractRepository setting) throws Exception {

		// verification
		HashMap<String, SMDAbstractRepository> plugins = PluginsUtils.getAll();

		if (plugins.get(setting.name) == null) {
			// todo find exception 404 in spring 4
			throw new IllegalArgumentException(" the plugin " + setting.name
					+ " dosn't exite");
		}

		SMDRepositoriesFactory.getInstance().save(setting);
	}

	/**
	 * start scan settings
	 * 
	 * @return
	 */
	@POST
	@Path("/start/{id}")
	public void start(@PathParam("id") String id) throws Exception {

		SMDAbstractRepository setting = getSettings(id);

		setting.start();
		SMDRepositoriesFactory.getInstance().save(setting);
	}

	/**
	 * start scan settings
	 * 
	 * @return
	 */
	@POST
	@Path("/stop/{id}")
	public void stop(@PathParam("id") String id) throws Exception {

		SMDAbstractRepository setting = getSettings(id);

		setting.start();
		SMDRepositoriesFactory.getInstance().save(setting);
	}

	/**
	 * 
	 * force to Scan All repositories
	 * 
	 * @return
	 */
	@POST
	public void post() throws Exception {
		new ScanDocuments().init();

	}
	
	
	
	
	private SMDAbstractRepository getSettings(String id) {
		SMDAbstractRepository plugin = SMDRepositoriesFactory.getInstance().get(
				id);

		if (plugin == null) {
			// todo find exception 404 in spring 4
			throw new NotFoundException(" the plugin withe the id :  "
					+ id + " doesn't exist");
		}

		return plugin;
	}
}
