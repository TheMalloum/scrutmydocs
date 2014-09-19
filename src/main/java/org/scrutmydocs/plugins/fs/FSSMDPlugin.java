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

package org.scrutmydocs.plugins.fs;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.contract.SMDResponseDocument;
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.plugins.SMDAbstractPlugin;
import org.scrutmydocs.plugins.SMDPlugin;
import org.scrutmydocs.search.SMDSearchFactory;

/**
 * Implement the DropBox ScrutMyDocs Data Source
 * 
 * @author Malloum LAYA
 * 
 */
@SMDPlugin(name = "fsDataSource")
public class FSSMDPlugin extends SMDAbstractPlugin {
	
	protected Logger logger = Logger.getLogger(getClass().getName());


	public FSSMDPlugin(String id, String url) {
		super();
		this.id = id;
		this.url = url;

	}

	public FSSMDPlugin() {
	}

	
	@Override
	public void scrut(Date lastModified) {
		try {
			
			logger.info("we are scrutting your dyrectory "+ url + " ....");

			List<Path> paths = parcourirDirectory(new File(this.url),
					lastModified);

			for (Path path : paths) {

				File file = path.toFile();

				if (!file.isDirectory()) {

					logger.debug("index  file "+ path);
					SMDDocument smdDocument = new SMDDocument(
							file.getAbsolutePath(),
							file.getName(),
							URLConnection
									.guessContentTypeFromStream(new FileInputStream(
											file)),
							FileUtils.readFileToByteArray(file), new Date(
									file.lastModified()));

					index(smdDocument);
				} else {
					logger.debug("cleanning directory "+ path + " ....");
					cleanDirectory(file);
				}

			}
		} catch (Exception ex) {

			throw new RuntimeException(
					"can't checkout changes in the directory " + this.url, ex);
		}
	}

	public List<Path> parcourirDirectory(File dir, Date lastModified) {

		if (!dir.isDirectory()) {

			throw new IllegalArgumentException(" dir must be a directory");
		}

		List<Path> paths = new ArrayList<Path>();

		for (File file : dir.listFiles()) {
			paths.add(file.toPath());

			if (file.isDirectory()) {
				for (Path path : parcourirDirectory(file, lastModified)) {
					paths.add(path);
				}

			}
		}

		return paths;
	}

	public void cleanDirectory(File directory) {
		
		int first = 0;
		int page =100;
		long total = 1;
		while(first < total){
			
			SMDSearchResponse searchResponse =  SMDSearchFactory.getInstance().searchFileByDirectory("*",first, page);
			for (SMDResponseDocument smdResponseDocument : searchResponse.smdDocuments) {
				if(!new File(smdResponseDocument.document.url).exists())
				{
					logger.debug("remove file "+ smdResponseDocument.document.url + " ....");
					this.delete(smdResponseDocument.document.url);
				}
			}
			
			total = searchResponse.totalHits;
			first =+ page;
		}
		
		
	}

	public static void main(String[] args) {

		File dir = new File("C:\\Users\\LAYA\\Downloads");
		Date lastModified = new Date();
		lastModified.setYear(0);

		List<Path> paths = new FSSMDPlugin().parcourirDirectory(dir,
				lastModified);

		for (Path path : paths) {
			System.out.println(path.toString());
		}

	}
}
