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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.contract.SMDRepositoryData;
import org.scrutmydocs.repositories.SMDRepositoriesFactory;

import com.sun.istack.NotNull;

@Path("/2/repositories")
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
		return Response.ok(
				SMDRepositoriesFactory.getInstance().getRepositories()).build();
	}

	/**
	 * Get all repositoy scan bu scrutmydocs
	 * 
	 * @return
	 */
	@Path("/_all_by_type/{type}")
	@GET
	public Response getAllByType(@PathParam("type") String type)
			throws Exception {

		List<SMDRepositoryData> reposytoriesType = new ArrayList<SMDRepositoryData>();

		List<SMDRepositoryData> reposytories = SMDRepositoriesFactory
				.getInstance().getRepositories();

		if (reposytories == null || reposytories.size() == 0) {
			return Response.ok(reposytoriesType).build();
		}

		for (SMDRepositoryData reposytory : reposytories) {
			if (reposytory.type.equals(type)) {

				reposytoriesType.add(reposytory);

			}
		}

		return Response.ok(reposytoriesType).build();
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
	 * Get settings
	 * 
	 * @return
	 */
	@GET
	@Path("field/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response filedStructure(@PathParam("type") String type)
			throws Exception {

		String tab[][];

		Class<? extends SMDRepositoryData> repo = SMDRepositoriesFactory
				.getTypeRepository(type);

		if (repo == null) {

			throw new NotFoundException("the repository doesn't existe ("
					+ type + ")");
		}
		tab = new String[repo.getDeclaredFields().length][3];

		int i = 0;
		for (Field field : repo.getDeclaredFields()) {
			tab[i][0] = field.getName();
			tab[i][1] = field.getType().toString();
			tab[i][2] = field.isAnnotationPresent(NotNull.class) ? "required"
					: "not required";
			i++;
		}

		return Response.ok(tab).build();
	}

	
	/**
	 * DELETE repositoy by id
	 * 
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") String id) throws Exception {

		SMDRepositoriesFactory.getInstance().deleteRepository(getSettings(id));

	}

	/**
	 * add or update a repositoy to scan on scrutmydocs
	 * 
	 * @param newRepository
	 * @throws Exception
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response put(SMDRepositoryData newRepository) throws Exception {

		if (newRepository.id == null || newRepository.id.trim().isEmpty()) {
			newRepository.id = UUID.randomUUID().toString();
		}else{
			delete(newRepository.id);
		}

		SMDRepositoriesFactory.getInstance().save(newRepository);
		
		
		return Response.ok(newRepository).build();
	}

	/**
	 * 
	 * @param stop
	 *            scan one repository
	 * @throws Exception
	 */
	@POST
	@Path("{id}/start")
	public void start(@PathParam("id") String id) throws Exception {

		SMDRepositoryData setting = getSettings(id);

		setting.start();
		SMDRepositoriesFactory.getInstance().save(setting);
	}

	/***
	 * stop scan one repository
	 * 
	 * @param id
	 * @throws Exception
	 */

	@POST
	@Path("/{id}/stop")
	public void stop(@PathParam("id") String id) throws Exception {

		SMDRepositoryData setting = getSettings(id);

		setting.stop();
		SMDRepositoriesFactory.getInstance().save(setting);
	}

	private SMDRepositoryData getSettings(String id) {
		SMDRepositoryData plugin = SMDRepositoriesFactory.getInstance().get(id);

		if (plugin == null) {
			throw new NotFoundException(" the repository  with the id :  " + id
					+ " doesn't exist");

		}

		return plugin;
	}

}
