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

import java.io.File;
import java.io.FileInputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.datasource.SMDDataSource;
import org.scrutmydocs.datasource.SMDRegister;

/**
 * Implement the DropBox ScrutMyDocs Data Source
 * 
 * @author Malloum LAYA
 * 
 */
@SMDRegister(name = "fsDataSource")
public class FSSMDDataSource extends SMDDataSource {

	private String protocol;
	private String server;
	private String username;
	private String password;

	public FSSMDDataSource(String id, String url) {
		super();
		this.id = id;
		this.url = url;

	}

	public FSSMDDataSource() {
	}

	protected Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public List<SMDDocument> changes(Date date) {
		List<SMDDocument> changes = new ArrayList<SMDDocument>();

		try {

			Collection<File> files = FileUtils.listFiles(new File(url), null,
					true);

			for (File file : files) {

				if (new Date(file.lastModified()).after(date) && file.isFile()) {

					// getContentType don't works

					SMDDocument smdDocument = new SMDDocument(
							file.getAbsolutePath(),
							file.getName(),
							URLConnection
									.guessContentTypeFromStream(new FileInputStream(
											file)),
							FileUtils.readFileToByteArray(file), new Date(
									file.lastModified()), null);

					changes.add(smdDocument);
				}
			}
		} catch (Exception ex) {

			throw new RuntimeException(
					"can't checkout changes in the directory " + this.url, ex);
		}

		return changes;
	}

	@Override
	public String getDocumentPath(String id) {
		return "file//" + id;
	}

	@Override
	public SMDDocument getDocument(String id) {
		File file = new File(id);
		SMDDocument smdDocument;
		try {
			smdDocument = new SMDDocument(file.getAbsolutePath(),
					file.getName(),
					URLConnection
							.guessContentTypeFromStream(new FileInputStream(
									file)),
					FileUtils.readFileToByteArray(file), new Date(
							file.lastModified()), null);
		} catch (Exception e) {
			throw new RuntimeException("can't checkout document " + id);
		}

		return smdDocument;
	}

}
