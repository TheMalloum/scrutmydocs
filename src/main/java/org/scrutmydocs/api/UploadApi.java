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

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.repositories.upload.UploadSMDPlugin;
import org.scrutmydocs.search.SMDSearchFactory;

@Path("/2/upload")
public class UploadApi {
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Add a new document
	 * 
	 * @return
	 */
	@POST
	public  void push(SMDDocument smdDocument) {
		SMDSearchFactory.getInstance()
				.index(new UploadSMDPlugin(), smdDocument);
	}

	/**
	 * Delete an existing document
	 * 
	 * @param id
	 * @return
	 */
	@DELETE
	public  void delete(String id) {
		SMDSearchFactory.getInstance().delete(new UploadSMDPlugin(), id);
	}
}
