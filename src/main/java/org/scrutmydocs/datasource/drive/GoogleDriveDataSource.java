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

package org.scrutmydocs.datasource.drive;

import java.util.List;

import org.scrutmydocs.datasource.data.SMDChanges;
import org.scrutmydocs.datasource.data.SMDDataSource;
import org.scrutmydocs.documents.SMDDocument;

/**
 * Implementation the DropBox ScrutMyDocs Data Source
 * 
 * @author Malloum LAYA
 * 
 */
public class GoogleDriveDataSource extends SMDDataSource {

	String token;
	String path;

	public GoogleDriveDataSource(SMDDocument token, SMDDocument path) {

	}

	@Override
	public List<SMDChanges> changes(String since) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentPath(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		return path;
	}

	@Override
	public SMDDocument getDocument(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
