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

package org.scrutmydocs.converters;

import org.scrutmydocs.domain.SMDConfiguration;
import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.exceptions.SMDExtractionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * This class process a binary content and generates from it a SMDDocument
 * using Apache Tika library
 */
public class FileToSMDDocument extends ObjectToSMDDocument {
    private static final Logger logger = LoggerFactory.getLogger(FileToSMDDocument.class);

    public static final String TYPE_FS = "fs";

    /**
     * Extract content from a file
     */
    public static SMDDocument toDocument(File file) throws SMDExtractionException {
        logger.debug("generating SMDDocument from [{}]", file.getAbsolutePath());
        try (InputStream is = new FileInputStream(file)) {
            return toDocument(
                    is,
                    TYPE_FS,
                    SMDConfiguration.INDEXED_CHARS_DEFAULT,
                    file.getName(),
                    file.getParent(),
                    new Date(file.lastModified()),
                    new Date(),
                    file.toPath().toUri().toString(),
                    file.length()
            );
        } catch (IOException e) {
            throw new SMDExtractionException(e);
        }
    }
}
