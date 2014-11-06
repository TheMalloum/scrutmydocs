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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * This class process a binary content and generates from it a SMDDocument
 * using Apache Tika library
 */
public class UploadToSMDDocument extends ObjectToSMDDocument {
    private static final Logger logger = LoggerFactory.getLogger(UploadToSMDDocument.class);

    public static final String TYPE_UPLOAD = "upload";

    /**
     * Extract content from a stream
     */
    public static SMDDocument toDocument(byte[] data, String filename) throws SMDExtractionException {
        logger.debug("generating SMDDocument from uploaded content [{}]", filename);

        try (InputStream is = new ByteArrayInputStream(data)) {
            return toDocument(
                    is,
                    TYPE_UPLOAD,
                    SMDConfiguration.INDEXED_CHARS_DEFAULT,
                    filename,
                    null,
                    null,
                    new Date(),
                    null, // TODO Generate an URL?
                    new Long(data.length)
            );
        } catch (IOException e) {
            throw new SMDExtractionException(e);
        }
    }
}
