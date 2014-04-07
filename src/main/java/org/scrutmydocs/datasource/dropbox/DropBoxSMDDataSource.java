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

package org.scrutmydocs.datasource.dropbox;

import java.util.Date;
import java.util.List;

import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.datasource.SMDDataSource;
import org.scrutmydocs.datasource.SMDRegister;
import org.scrutmydocs.webapp.util.StringTools;

/**
 * Manage DropBoxRiver Rivers metadata
 * 
 * @author PILATO
 * 
 */
@SMDRegister(name = "dropbox")
public class DropBoxSMDDataSource extends SMDDataSource {
	private static final long serialVersionUID = 1L;

	private String token;
	private String secret;

	/**
	 * @param id
	 *            The unique id of this river
	 * @param token
	 *            Dropbox Token
	 * @param secret
	 *            Dropbox Secret
	 * @param url
	 *            URL where to fetch content
	 * @param updateRate
	 *            Update Rate (in seconds)
	 */
	public DropBoxSMDDataSource(String id, String token, String secret,
			String url, Long updateRate) {
		super(id, url, updateRate);
		this.token = token;
		this.secret = secret;
	}

	/**
	 * @return DropBox Token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            DropBox Token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return DropBox Secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param secret
	 *            DropBox Secret
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public String toString() {
		return StringTools.toString(this);
	}

	@Override
	public List<SMDDocument> changes(Date since) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentPath(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SMDDocument getDocument(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
