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
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.contract.SMDFileDocument;
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.repositories.SMDRepositoriesFactory;
import org.scrutmydocs.repositories.SMDRepositoryData;
import org.scrutmydocs.repositories.SMDRepositoryScan;
import org.scrutmydocs.repositories.annotations.SMDRegisterRepositoryScan;
import org.scrutmydocs.search.SMDSearchFactory;

/**
 * Implement the DropBox ScrutMyDocs Data Source
 * 
 * @author Malloum LAYA
 * 
 */
@SMDRegisterRepositoryScan(name = "fs")
public class FSSMDRepositoryScan extends SMDRepositoryScan {

	protected org.apache.logging.log4j.Logger logger = LogManager
			.getLogger(getClass().getName());

	@Override
	public void scrut(SMDRepositoryData data) {
		
		
		if(! (data instanceof FSSMDRepositoryData)) throw new IllegalArgumentException("SMDRepositoryData is not a FSSMDRepositoryData");
		FSSMDRepositoryData fssmdRepositoryData = (FSSMDRepositoryData)data;
		
		
		try {

			logger.info("we are scrutting your dyrectory " + fssmdRepositoryData.url + " ....");

			Date startScarn = new Date();

			List<Path> paths = parcourirDirectory(new File(fssmdRepositoryData.url),
					fssmdRepositoryData.lastScan);

			for (Path path : paths) {

				File file = path.toFile();

				if (file.isFile()) {

					logger.debug("index  file " + path);

					SMDFileDocument smdDocument;
					try {
						smdDocument = new SMDFileDocument(file);
					} catch (FileNotFoundException e) {
						continue;
					}

					SMDSearchFactory.getInstance().index(fssmdRepositoryData, smdDocument);
				} else {
					logger.debug("cleanning directory " + path + " ....");
					// if the directory changes we must to find if documents
					// were removed
					cleanDirectory(file,fssmdRepositoryData);

					logger.debug("cleanning directory " + path + " ....");
					// if the directory changes we must index all files. ( a old
					// document can be moved without change a last modified
					// date)
					indexAllFiles(file,fssmdRepositoryData);
				}

			}

			fssmdRepositoryData.lastScan = startScarn;
			SMDRepositoriesFactory.getInstance().save(fssmdRepositoryData);

		} catch (Exception ex) {

			throw new RuntimeException(
					"can't checkout changes in the directory " + fssmdRepositoryData.url, ex);
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

			// if (lastModified == null
			// || new Date(file.lastModified()).after(lastModified)) {
			// logger.trace("find one File to index : " + file.toPath());
			// paths.add(file.toPath());
			// }

			if (file.isDirectory()) {
				for (Path path : parcourirDirectory(file, lastModified)) {
					paths.add(path);
				}

			}
		}

		return paths;
	}

	public void cleanDirectory(File dir,FSSMDRepositoryData fssmdRepositoryData) {

		if (dir.isFile()) {
			throw new IllegalArgumentException(
					"we can cleanDirectory just to a directory");
		}

		int first = 0;
		int page = 100;
		long total = 1;
		while (first < total) {

			SMDSearchResponse searchResponse = SMDSearchFactory.getInstance()
					.searchFileByDirectory(fssmdRepositoryData, dir.getAbsolutePath(), first,
							page);
			for (SMDDocument smdResponseDocument : searchResponse.smdDocuments) {
				if (!new File(smdResponseDocument.url).exists()) {
					logger.debug("remove file " + smdResponseDocument.url
							+ " ....");
					SMDSearchFactory.getInstance().delete(fssmdRepositoryData,
							smdResponseDocument.url);
				}
			}

			total = searchResponse.totalHits;
			first = +page;
		}

	}

	private void indexAllFiles(File dir,FSSMDRepositoryData fssmdRepositoryData) throws IOException {

		if (dir.isFile()) {
			throw new IllegalArgumentException(
					"we can indexAllFiles just to a directory");
		}

		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				try {
					SMDSearchFactory.getInstance().index(fssmdRepositoryData,
							new SMDFileDocument(file));
				} catch (FileNotFoundException e) {
					continue;
				}
			}
		}

	}

}
