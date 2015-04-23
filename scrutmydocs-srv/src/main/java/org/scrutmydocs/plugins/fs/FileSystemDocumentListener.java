/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.plugins.DocumentListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import restx.factory.Component;

import com.google.common.collect.ImmutableList;

@Component
public class FileSystemDocumentListener implements DocumentListener<File> {
    protected static final Logger logger = LoggerFactory.getLogger(FileSystemDocumentListener.class);

    private List<Path> paths = new ImmutableList.Builder<Path>().build();
    private DateTime lastChecked;
    private FileSystemConverter converter;

    @Inject
    public FileSystemDocumentListener(FileSystemConverter converter) {
        this.converter = converter;
    }

    public void setLastChecked(DateTime lastChecked) {
        this.lastChecked = lastChecked;
    }

    /**
     * Call this method when you want to add another path to scrut
     */
    public void add(Path path) {
        ImmutableList.Builder<Path> pathBuilder = new ImmutableList.Builder<>();
        pathBuilder.addAll(this.paths);
        pathBuilder.add(path);
        this.paths = pathBuilder.build();
    }

    /**
     * Call this method when you want to remove a path
     */
    public void remove(Path path) {
        ImmutableList.Builder<Path> pathBuilder = new ImmutableList.Builder<Path>();

        for (Path currentPath : paths) {
            if (path.compareTo(currentPath) != 0) {
                pathBuilder.add(currentPath);
            }
        }

        this.paths = pathBuilder.build();
    }

    @Override
    public List<String> scrut()  {
        DateTime checkingDate = DateTime.now();
        logger.debug("current date is [{}]", checkingDate);
        final List<String> files = new ArrayList<>();

        for (Path path : paths) {
            try {
                logger.debug("checking path [{}]", path);
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes fileAttributes) throws IOException {
                        logger.trace("visiting file [{}]", file);

                        // If this file is new, let's add it
                        DateTime fileDate = new DateTime(fileAttributes.lastModifiedTime().toMillis());
                        if (fileDate.isAfter(lastChecked)) {
                            files.add(file.toAbsolutePath().toString());
                        } else {
                            logger.debug("ignoring old file [{}] date [{}]", file, fileDate);
                        }

                        return FileVisitResult.CONTINUE;
                    }
                });

                // TODO We need to manage file removal...
            } catch (IOException e) {
                // We need to be smarter here. Because some paths could work and some others not.
                logger.warn("caught an error while scanning [{}]: [{}]", path, e.getMessage());
            }
        }

        lastChecked = checkingDate;
        return files;
    }

    @Override
    public SMDDocument get(String documentId)  {
        Path file = Paths.get(documentId);
        logger.debug("add new file [{}]", file.toAbsolutePath());
        SMDDocument smdDocument = converter.toDocument(file.toFile());
        logger.trace("generated document: [{}]", smdDocument);
        return smdDocument;
    }

    @Override
    public void delete(File document)  {
        logger.debug("delete file [{}]", document.getAbsolutePath());
        SMDDocument smdDocument = converter.toDocument(document);
        logger.trace("generated document: [{}]", smdDocument);
    }
}
