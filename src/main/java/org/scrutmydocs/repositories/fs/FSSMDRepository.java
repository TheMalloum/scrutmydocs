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

package org.scrutmydocs.repositories.fs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.scrutmydocs.contract.SMDRepository;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.contract.SMDResponseDocument;
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.repositories.SMDRepositoryRegister;
import org.scrutmydocs.repositories.SMDRepositoriesFactory;
import org.scrutmydocs.search.SMDSearchFactory;

/**
 * Implement the DropBox ScrutMyDocs Data Source
 * 
 * @author Malloum LAYA
 * 
 */
@SMDRepositoryRegister(name = "fsDataSource")
public class FSSMDRepository extends SMDRepository {

	public FSSMDRepository(String url) {
		super();
		this.url = url;
	}
	
	public FSSMDRepository() {
		super();
	}
	


	protected org.apache.logging.log4j.Logger logger = LogManager
			.getLogger(getClass().getName());


	@Override
	public void scrut() {
		try {

			logger.info("we are scrutting your dyrectory " + url + " ....");

			Date startScarn = new Date();

			List<Path> paths = parcourirDirectory(new File(this.url),
					this.lastScan);

			for (Path path : paths) {

				File file = path.toFile();

				if (file.isFile()) {

					logger.debug("index  file " + path);
					SMDDocument smdDocument = new SMDDocument(file);

					SMDSearchFactory.getInstance().index(this, smdDocument);
				} else {
					logger.debug("cleanning directory " + path + " ....");
					//if the directory changes we must to find if documents were removed
					cleanDirectory(file);
					
					
					logger.debug("cleanning directory " + path + " ....");
					//if the directory changes we must index all files. ( a old document can be moved without change a last modified date)
					indexAllFiles(file);
				}

			}

			this.lastScan = startScarn;
			SMDRepositoriesFactory.getInstance().save(this);

		} catch (Exception ex) {

			throw new RuntimeException(
					"can't checkout changes in the directory " + this.url, ex);
		}
	}

	public List<Path> parcourirDirectory(File dir, Date lastModified) {

		if (dir.isFile()) {

			throw new IllegalArgumentException(" dir must be a directory");
		}

		List<Path> paths = new ArrayList<Path>();

		if (lastModified == null
				|| new Date(dir.lastModified()).after(lastModified)) {
			paths.add(dir.toPath());
		}

		for (File file : dir.listFiles()) {

			if (lastModified == null
					|| new Date(file.lastModified()).after(lastModified)) {
				logger.trace("find one File to index : " + file.toPath());
				paths.add(file.toPath());
			}

			if (file.isDirectory()) {
				for (Path path : parcourirDirectory(file, lastModified)) {
					paths.add(path);
				}

			}
		}

		return paths;
	}

	public void cleanDirectory(File dir) {

		
		if(dir.isFile()){
			throw new IllegalArgumentException ("we can cleanDirectory just to a directory");
		} 
		
		int first = 0;
		int page = 100;
		long total = 1;
		while (first < total) {

			SMDSearchResponse searchResponse = SMDSearchFactory.getInstance()
					.searchFileByDirectory(this, dir.getAbsolutePath(),
							first, page);
			for (SMDResponseDocument smdResponseDocument : searchResponse.smdDocuments) {
				if (!new File(smdResponseDocument.url).exists()) {
					logger.debug("remove file " + smdResponseDocument.url
							+ " ....");
					SMDSearchFactory.getInstance().delete(this,
							smdResponseDocument.url);
				}
			}

			total = searchResponse.totalHits;
			first = +page;
		}

	}
	
	
	private void indexAllFiles(File dir) throws FileNotFoundException, IOException  {

		if(dir.isFile()){
			throw new IllegalArgumentException ("we can indexAllFiles just to a directory");
		}
		
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				SMDSearchFactory.getInstance().index(this,
						new SMDDocument(file));
			}
		}

	}


}
