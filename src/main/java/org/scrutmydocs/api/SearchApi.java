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

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.spi.NotImplementedYetException;
import org.scrutmydocs.contract.SMDRepository;
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.contract.SMDSearchQuery;
import org.scrutmydocs.search.SMDSearchFactory;
/***
 * This API provide access to the document search
 * @author LAYA
 *
 */
@Path("/2/search")
public class SearchApi {
	protected final Log logger = LogFactory.getLog(getClass());

	public Api[] helpApiList() {
		Api[] apis = new Api[1];
		apis[0] = new Api("/1/search", "POST", "Search for documents");
		return apis;
	}

	public String helpMessage() {
		return "The /1/search API helps you to search your documents.";
	}

	/**
	 * like google, Search for all documents with one word 
	 * 
	 * @param query
	 * @return
	 */
	@POST
	public Response search(SMDSearchQuery query) {
		SMDSearchResponse results = null;

		results = SMDSearchFactory.getInstance().search(query.getSearch(),
				query.getFirst(), query.getPageSize());

		return Response.ok(results).build();
	}
	
	
}
