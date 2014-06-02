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
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.search.SMDSearchFactory;
import org.scrutmydocs.webapp.CommonBaseApi;
import org.scrutmydocs.webapp.api.search.data.SearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller(value = "SearchApiV2")
@RequestMapping("/2/search")
public class SearchApi extends CommonBaseApi {
	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	public Api[] helpApiList() {
		Api[] apis = new Api[1];
		apis[0] = new Api("/1/search", "POST", "Search for documents");
		return apis;
	}

	@Override
	public String helpMessage() {
		return "The /1/search API helps you to search your documents.";
	}

	/**
	 * Search for documents
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
    SMDSearchResponse term(@RequestBody SearchQuery query) {
		SMDSearchResponse results = null;

		results = SMDSearchFactory.getInstance().search(query.getSearch(),
				query.getFirst(), query.getPageSize());

		return results;
	}

}
