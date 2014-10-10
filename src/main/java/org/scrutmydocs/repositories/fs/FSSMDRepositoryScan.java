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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.contract.SMDFileDocument;
import org.scrutmydocs.contract.SMDRepositoryData;
import org.scrutmydocs.contract.SMDRepositoryScan;
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
	public synchronized void  scrut(SMDRepositoryData data) {

		if (!(data instanceof FSSMDRepositoryData)) {
			logger.error("SMDRepositoryData is not a FSSMDRepositoryData");
			throw new IllegalArgumentException(
					"SMDRepositoryData is not a FSSMDRepositoryData");

		}
		FSSMDRepositoryData fssmdRepositoryData = (FSSMDRepositoryData) data;

		if (!new File(fssmdRepositoryData.url).isDirectory()) {
			logger.warn("we cant scru just a directory and "
					+ fssmdRepositoryData.url + " isn't a directory");
			throw new IllegalArgumentException(
					"we cant scru just a directory and "
							+ fssmdRepositoryData.url + " isn't a directory");
		}

		try {

			logger.info("we are scrutting your directory "
					+ fssmdRepositoryData.url + " ....");

			File root = new File(fssmdRepositoryData.url);

			if ((fssmdRepositoryData.lastScan == null || new Date(
					root.lastModified()).after(fssmdRepositoryData.lastScan))) {

				indexAllFiles(root, fssmdRepositoryData);
			}

			List<Path> paths = browseDirectory(
					new File(fssmdRepositoryData.url),
					fssmdRepositoryData.lastScan);

			for (Path path : paths) {

				File file = path.toFile();

				if (file.isDirectory()) {
					
					logger.info("cleanning directory and index of " + path
							+ " ");
					// if the directory changes we must to index all the
					// directory

					

					indexAllFiles(file, fssmdRepositoryData);
				}

			}

		} catch (Exception ex) {

			throw new RuntimeException(
					"can't checkout changes in the directory "
							+ fssmdRepositoryData.url, ex);
		}
	}

	public List<Path> browseDirectory(File dir, Date lastModified) {

		logger.trace("parcourir dir : " + dir.getPath());

		if (!dir.isDirectory()) {

			throw new IllegalArgumentException(" dir must be a directory");
		}

		List<Path> paths = new ArrayList<Path>();

		File[] listfile = dir.listFiles();
		if (listfile.length == 0)
			return paths;

		for (File file : listfile) {

//			if (lastModified == null
//					|| new Date(file.lastModified()).after(lastModified)) {
//				logger.trace("find one File to index : " + file.toPath());
//				paths.add(file.toPath());
//			}

			if ( file.isDirectory() && (lastModified == null || new Date(file.lastModified())
							.after(lastModified))) {
					paths.add(file.toPath());
					for (Path path : browseDirectory(file, lastModified)) {
						paths.add(path);
				}

			}
		}

		return paths;
	}

	private void indexAllFiles(File dir, FSSMDRepositoryData fssmdRepositoryData)
			throws IOException {

		if (dir.isFile()) {
			throw new IllegalArgumentException(
					"we can indexAllFiles just to a directory");
		}

		SMDSearchFactory.getInstance().deleteDirectory(
				dir.getPath());
		
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				try {
					SMDSearchFactory.getInstance().index(
							new SMDFileDocument(fssmdRepositoryData, file,
									fssmdRepositoryData.type));
				} catch (FileNotFoundException e) {
					logger.error("the document {} could not be index {}",file.getAbsoluteFile(),e);
					continue;
				}
			}
		}

	}

	public String getpath(File file) {
		return "file://" + file.getPath();
	}

	@Override
	public boolean check(SMDRepositoryData data) {

		if (!(data instanceof FSSMDRepositoryData)) {
			logger.error("SMDRepositoryData is not a FSSMDRepositoryData");
			throw new IllegalArgumentException(
					"SMDRepositoryData is not a FSSMDRepositoryData");

		}
		FSSMDRepositoryData fssmdRepositoryData = (FSSMDRepositoryData) data;

		try {

			if (new File(fssmdRepositoryData.url).isDirectory()) {
				return true;
			} else {
				logger.error("the repository {} is not available",
						fssmdRepositoryData.url);
				return false;
			}

		} catch (Exception e) {
			logger.error("the repository {} is not available",
					fssmdRepositoryData.url);
			return false;
		}

	}

	@Override
	public byte[] get(SMDDocument document) {

		InputStream is;
		try {
			is = new FileInputStream(new File(document.url));
		} catch (FileNotFoundException e) {
			return null;
		}

		try {
			return IOUtils.toByteArray(is);
		} catch (IOException e) {
			return null;
		}

	}

	@Override
	public void delete(SMDRepositoryData data) {
		if (!(data instanceof FSSMDRepositoryData)) {
			logger.error("SMDRepositoryData is not a FSSMDRepositoryData");
			throw new IllegalArgumentException(
					"SMDRepositoryData is not a FSSMDRepositoryData");

		}
		FSSMDRepositoryData fssmdRepositoryData = (FSSMDRepositoryData) data;

		SMDSearchFactory.getInstance().deleteDirectory(fssmdRepositoryData.url);
	}

}
