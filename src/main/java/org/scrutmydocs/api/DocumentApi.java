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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.datasource.upload.UploadDataSource;
import org.scrutmydocs.search.SMDSearchFactory;
import org.scrutmydocs.webapp.api.common.data.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/2/doc")
public class DocumentApi extends CommonBaseApi {
	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	public Api[] helpApiList() {
		Api[] apis = new Api[7];
		apis[0] = new Api("/1/doc", "POST",
				"Add a document to the search engine");
		apis[1] = new Api("/1/doc/{id}", "DELETE",
				"Delete a document in the default index/type (doc/docs)");
		apis[2] = new Api("/1/doc/{index}/{id}", "DELETE",
				"Delete a document in the default type (doc)");
		apis[3] = new Api("/1/doc/{index}/{type}/{id}", "DELETE",
				"Delete a document ");
		apis[4] = new Api("/1/doc/{id}", "GET",
				"Get a document in the default index/type (doc/docs)");
		apis[5] = new Api("/1/doc/{index}/{id}", "GET",
				"Get a document in a specific index with default type (docs)");
		apis[6] = new Api("/1/doc/{index}/{type}/{id}", "GET",
				"Get a document in a specific index/type");
		return apis;
	}

	@Override
	public String helpMessage() {
		return "The /1/doc API helps you to manage your documents.";
	}

	/**
	 * Add a new document
	 * 
	 * @param doc
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	void push(@RequestBody SMDDocument smdDocument) {
		SMDSearchFactory.getInstance(new UploadDataSource()).index(smdDocument);
	}

	/**
	 * Delete an existing document
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public @ResponseBody
	void delete(@PathVariable String id) {
		SMDSearchFactory.getInstance().delete(id);
	}

	/**
	 * Delete an existing document
	 * 
	 * @param id
	 * @return
	 * @deprecated
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "{index}/{id}")
	public @ResponseBody
	void delete(@PathVariable String index, @PathVariable String id) {
		throw new NotImplementedException(
				"This methode is depreacated, use /{id} for delete a document");
	}

	/**
	 * Delete an existing document
	 * 
	 * @param id
	 * @return
	 * @deprecated
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "{index}/{type}/{id}")
	public @ResponseBody
	void delete(@PathVariable String index, @PathVariable String type,
			@PathVariable String id) {
		throw new NotImplementedException(
				"This methode is depreacated, use /{id} for delete a document");
	}

	/**
	 * Get a document with its coordinates (index, type, id)
	 * 
	 * @param index
	 * @param type
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{index}/{type}/{id}")
	public @ResponseBody
	SMDDocument get(@PathVariable String index, @PathVariable String type,
			@PathVariable String id) {

		throw new NotImplementedException(
				"This methode is depreacated, the url documente must be use to get the document");
	}

	/**
	 * Get a document of type "doc" in a given index knowing its id
	 * 
	 * @param index
	 * @param id
	 * @return
	 * @deprecated
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{index}/{id}")
	public @ResponseBody
	SMDDocument get(@PathVariable String index, @PathVariable String id) {
		throw new NotImplementedException(
				"This methode is depreacated, the url documente must be use to get the document");
	}

	/**
	 * Get a document in the default index/type (docs/doc) knowing its id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public @ResponseBody
	SMDDocument get(@PathVariable String id) {
		throw new NotImplementedException(
				"This methode is depreacated, the url documente must be use to get the document");
	}

}
