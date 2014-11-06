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

package org.scrutmydocs.plugins;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.elasticsearch.common.Strings;
import org.joda.time.DateTime;
import org.scrutmydocs.domain.SMDConfiguration;
import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.exceptions.SMDExtractionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Date;

/**
 * This abstract class provides default Tika implementation to extract content
 * from binary files.
 */
public abstract class AbstractTikaPlugin<T> implements Plugin<T> {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractTikaPlugin.class);

    protected final Tika tika;

    // TODO Inject Tika here with a SMDTikaService
    public AbstractTikaPlugin() {
        tika = new Tika();
    }

    /**
     * Extract content from a stream
     */
    protected SMDDocument toDocument(InputStream is,
                                         String type,
                                         int indexedChars,
                                         String filename,
                                         String path,
                                         Date lastModified,
                                         Date indexedDate,
                                         String url,
                                         Long filesize) throws SMDExtractionException {
        Metadata metadata = new Metadata();

        String parsedContent;
        try {
            // Set the maximum length of strings returned by the parseToString method, -1 sets no limit
            parsedContent = tika.parseToString(is, metadata, indexedChars);
        } catch (Throwable e) {
            logger.debug("Failed to extract [{}] characters of text", indexedChars);
            logger.trace("exception raised", e);
            throw new SMDExtractionException(e);
        }

        SMDDocument smdFileDocument = new SMDDocument();

        // File
        smdFileDocument.file.filename = filename;
        smdFileDocument.file.path = path;
        smdFileDocument.file.last_modified = lastModified;
        smdFileDocument.file.indexing_date = indexedDate;
        smdFileDocument.file.url = url;
        smdFileDocument.file.content_type = metadata.get(Metadata.CONTENT_TYPE);

        // We only add `indexed_chars` if we have other value than default
        if (indexedChars != SMDConfiguration.INDEXED_CHARS_DEFAULT) {
            smdFileDocument.file.indexed_chars = indexedChars;
        }

        if (metadata.get(Metadata.CONTENT_LENGTH) != null) {
            // We try to get CONTENT_LENGTH from Tika first
            smdFileDocument.file.filesize = Long.parseLong(metadata.get(Metadata.CONTENT_LENGTH));
        } else {
            // Otherwise, we use provided length
            smdFileDocument.file.filesize = filesize;
        }

        // Metadata
        smdFileDocument.meta.author = metadata.get(Metadata.AUTHOR);
        smdFileDocument.meta.title = metadata.get(Metadata.TITLE);
        if (metadata.get(Metadata.DATE) != null) {
            smdFileDocument.meta.date = DateTime.parse(metadata.get(Metadata.DATE)).toDate();
        }
        smdFileDocument.meta.keywords = Strings.commaDelimitedListToStringArray(metadata.get(Metadata.KEYWORDS));

        // Doc content
        smdFileDocument.content = parsedContent;

        // Doc type
        smdFileDocument.type = type;

        return smdFileDocument;
    }
}
