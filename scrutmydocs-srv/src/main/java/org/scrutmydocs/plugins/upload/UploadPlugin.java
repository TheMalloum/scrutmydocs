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

package org.scrutmydocs.plugins.upload;

import org.scrutmydocs.domain.SMDConfiguration;
import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.exceptions.SMDException;
import org.scrutmydocs.exceptions.SMDExtractionException;
import org.scrutmydocs.plugins.AbstractTikaPlugin;
import restx.factory.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * This plugin processes an uploaded stream
 */
@Component
public class UploadPlugin extends AbstractTikaPlugin<File> {

    public static final String TYPE_UPLOAD = "upload";

    @Override
    public SMDDocument toDocument(File source) throws SMDException {
        logger.debug("generating SMDDocument from [{}]", source.getAbsolutePath());
        try (InputStream is = new FileInputStream(source)) {
            return toDocument(
                    is,
                    TYPE_UPLOAD,
                    SMDConfiguration.INDEXED_CHARS_DEFAULT,
                    source.getName(),
                    source.getParent(),
                    new Date(source.lastModified()),
                    new Date(),
                    source.toPath().toUri().toString(),
                    source.length()
            );
        } catch (IOException e) {
            throw new SMDExtractionException(e);
        }
    }

    @Override
    public String type() {
        return TYPE_UPLOAD;
    }

    @Override
    public String name() {
        return "Web Upload Plugin";
    }

    @Override
    public String version() {
        // Todo inject current version number
        return "N/A";
    }
}
