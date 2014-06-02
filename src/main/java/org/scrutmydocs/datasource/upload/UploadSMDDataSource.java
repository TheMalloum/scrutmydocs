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

package org.scrutmydocs.datasource.upload;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.datasource.SMDDataSource;
import org.scrutmydocs.datasource.SMDRegister;

/**
 * Implement the DropBox ScrutMyDocs Data Source
 * 
 * @author Malloum LAYA
 * 
 */
@SMDRegister(name = "uploadDataSource")
public class UploadSMDDataSource extends SMDDataSource {

	protected Logger logger = Logger.getLogger(getClass().getName());

	public UploadSMDDataSource() {
		super();
		this.id = "only-one";
	}

	@Override
	public List<SMDDocument> changes(Date date) {

		return null;
	}

	@Override
	public String getDocumentPath(String id) {
		return "file://" + id;
	}

	@Override
	public SMDDocument getDocument(String id) {

		return null;
	}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

}