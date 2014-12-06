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

import org.scrutmydocs.converters.IdGeneratorService;
import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.exceptions.SMDException;
import org.scrutmydocs.exceptions.SMDExtractionException;
import org.scrutmydocs.plugins.tika.TikaConverter;
import org.scrutmydocs.plugins.tika.TikaService;
import org.scrutmydocs.services.SMDConfigurationService;
import restx.factory.Component;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Component
public class FileSystemConverter extends TikaConverter<File> {

    @Inject
    public FileSystemConverter(TikaService tikaService,
                               SMDConfigurationService smdConfigurationService,
                               IdGeneratorService idGeneratorService) {
        super(tikaService, smdConfigurationService, idGeneratorService);
    }

    @Override
    public SMDDocument toDocument(File source) throws SMDException {
        logger.debug("generating SMDDocument from [{}]", source.getAbsolutePath());
        try (InputStream is = new FileInputStream(source)) {
            return toDocument(
                    is,
                    source.getAbsolutePath(),
                    FileSystemPlugin.TYPE_FS,
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
}
