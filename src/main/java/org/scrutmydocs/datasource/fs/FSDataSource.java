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

package org.scrutmydocs.datasource.fs;

import java.util.Date;
import java.util.List;

import org.scrutmydocs.contract.SMDChanges;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.datasource.SMDDataSource;
import org.scrutmydocs.datasource.SMDRegister;

/**
 * Implementation the DropBox ScrutMyDocs Data Source
 * 
 * @author Malloum LAYA
 * 
 */
@SMDRegister(name = "fsDataSource")
public class FSDataSource extends SMDDataSource {

	@Override
	public List<SMDChanges> changes(Date date) {
		return null;
	}

	@Override
	public String getDocumentPath(String id) {
		return null;
	}

	@Override
	public String id() {
		return null;
	}

	@Override
	public SMDDocument getDocument(String id) {
		return null;
	}



	@Override
	public Date since() {
		// TODO Auto-generated method stub
		return null;
	}

}
