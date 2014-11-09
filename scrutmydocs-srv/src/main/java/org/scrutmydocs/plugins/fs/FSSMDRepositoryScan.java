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

import org.apache.commons.io.IOUtils;
import org.scrutmydocs.annotations.SMDRegisterRepositoryScan;
import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.domain.SMDRepository;
import org.scrutmydocs.domain.SMDRepositoryScan;
import org.scrutmydocs.exceptions.SMDException;
import org.scrutmydocs.exceptions.SMDExtractionException;
import org.scrutmydocs.services.SMDDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Component;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implement the FS ScrutMyDocs Data Source
 * 
 * @author Malloum LAYA
 * 
 */
@Component
@SMDRegisterRepositoryScan(name = FSSMDRepository.TYPE)
public class FSSMDRepositoryScan extends SMDRepositoryScan {

    private static final Logger logger = LoggerFactory.getLogger(FSSMDRepositoryScan.class);

    private final SMDDocumentService searchService;
    private final FileSystemConverter fileSystemConverter;

    @Inject
    public FSSMDRepositoryScan(SMDDocumentService searchService,
                                    FileSystemConverter fileSystemConverter) {
        this.searchService = searchService;
        this.fileSystemConverter = fileSystemConverter;
    }

	@Override
	public synchronized void scrut(SMDRepository data) {

		if (!(data instanceof FSSMDRepository)) {
			logger.error("SMDRepositoryData is not a FSSMDRepositoryData");
			throw new IllegalArgumentException(
					"SMDRepositoryData is not a FSSMDRepositoryData");

		}
		FSSMDRepository fssmdRepositoryData = (FSSMDRepository) data;

		if (!new File(fssmdRepositoryData.url).isDirectory()) {
			logger.warn("we can scrut just a directory and "
					+ fssmdRepositoryData.url + " isn't a directory");
			throw new IllegalArgumentException(
					"we can scrut just a directory and "
							+ fssmdRepositoryData.url + " isn't a directory");
		}

		try {

			logger.info("Start scrutting your directory [{}]", fssmdRepositoryData.url);

			File root = new File(fssmdRepositoryData.url);

			if ((fssmdRepositoryData.lastScan == null || new Date(
					root.lastModified()).after(fssmdRepositoryData.lastScan))) {
				indexAllFiles(root);
			}

			List<Path> paths = browseDirectory(
					new File(fssmdRepositoryData.url),
					fssmdRepositoryData.lastScan);

			for (Path path : paths) {
				File file = path.toFile();
				if (file.isDirectory()) {
					logger.info("cleaning directory and index for [{}]", path);
					// if the directory changes we must to index all the
					// directory
					indexAllFiles(file);
				}
			}

			logger.info("End scrutting your directory {}", fssmdRepositoryData.url);

		} catch (Exception ex) {
			logger.error("scrutting your directory {} : {} ",
					fssmdRepositoryData.url, ex);

			throw new RuntimeException(
					"can't checkout changes in the directory "
							+ fssmdRepositoryData.url, ex);
		}
	}

	public List<Path> browseDirectory(File dir, Date lastModified) {

		logger.trace("browse dir : {}", dir.getPath());

		if (!dir.isDirectory()) {
			throw new IllegalArgumentException(" dir must be a directory");
		}

		List<Path> paths = new ArrayList<Path>();

		File[] files = dir.listFiles();
		if (files.length == 0)
			return paths;

		for (File file : files) {
			if (file.isDirectory()
					&& (lastModified == null || new Date(file.lastModified())
							.after(lastModified))) {
				paths.add(file.toPath());
			}
			if (file.isDirectory()) {
				for (Path path : browseDirectory(file, lastModified)) {
					paths.add(path);
				}
			}
		}

		return paths;
	}

	private void indexAllFiles(File dir)
            throws IOException, SMDException {

		if (dir.isFile()) {
			throw new IllegalArgumentException("we can indexAllFiles just to a directory");
		}

        searchService.deleteDirectory(dir.getPath());

		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				try {
                    searchService.index(fileSystemConverter.toDocument(file));
				} catch (SMDExtractionException e) {
                    logger.warn("can not index [{}]: {}", file, e);
                }
            }
		}
	}


	public String getpath(File file) {
		return "file://" + file.getPath();
	}

	@Override
	public boolean check(SMDRepository data) {

		if (!(data instanceof FSSMDRepository)) {
			logger.error("SMDRepositoryData is not a FSSMDRepositoryData");
			throw new IllegalArgumentException(
					"SMDRepositoryData is not a FSSMDRepositoryData");

		}
		FSSMDRepository fssmdRepositoryData = (FSSMDRepository) data;

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
			is = new FileInputStream(new File(document.file.url));
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
	public void delete(SMDRepository data) {
		if (!(data instanceof FSSMDRepository)) {
			logger.error("SMDRepositoryData is not a FSSMDRepositoryData");
			throw new IllegalArgumentException(
					"SMDRepositoryData is not a FSSMDRepositoryData");

		}
		FSSMDRepository fssmdRepositoryData = (FSSMDRepository) data;

        searchService.deleteDirectory(fssmdRepositoryData.url);
	}

}
