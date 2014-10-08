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

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.contract.SMDFileDocument;
import org.scrutmydocs.search.SMDSearchFactory;

@Path("/2/documents")
public class SMDDocumentApi {
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Get settings TODO
	 * 
	 * @return
	 */
	@GET
	@Path("/{id}")
	public Response getDocument(@PathParam("id") String id) throws Exception {

		SMDFileDocument smdFileDocument = SMDSearchFactory.getInstance()
				.getDocument(id);

		
		
		if(smdFileDocument==null){
			throw new NotFoundException(" the document with the id :  " + id
					+ " doesn't exist");
		}
		
		ResponseBuilder response = Response.ok(Base64.decodeBase64(smdFileDocument.source));
		response.header("Content-Disposition", "attachment; filename="
				+ smdFileDocument.name);
		
		return response.build();
	}

}
