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

package org.scrutmydocs.api.rivers;

import java.util.List;

import org.scrutmydocs.api.CommonBaseApi;
import org.scrutmydocs.datasource.SMDDataSource;
import org.scrutmydocs.search.SMDSearchFactory;
import org.scrutmydocs.webapp.api.settings.rivers.drive.data.RestResponseDriveRiver;

public abstract class CommonRiversApi extends CommonBaseApi {

	public SMDRestResponse get(SMDDataSource smdDataSource) {

		List<SMDDataSource> rivers = SMDSearchFactory.getInstance().getConf(
				smdDataSource);

		return new SMDRestResponse(rivers);
	}

	public SMDRestResponse get(SMDDataSource smdDataSource, final String id) {

		SMDDataSource river = SMDSearchFactory.getInstance().getConf(
				smdDataSource, id);

		return new SMDRestResponse(river);
	}

	public SMDRestResponse put(SMDDataSource river) {

		SMDSearchFactory.getInstance().saveConf(river);

		return new SMDRestResponse(river);
	}

	public SMDRestResponse delete(String id) {

		SMDSearchFactory.getInstance().delete(id);

		return new SMDRestResponse();
	}

	public SMDRestResponse start(String id) {
		return new SMDRestResponse();

	}

	public SMDRestResponse stop(String id) {
		return new SMDRestResponse();
	}

}