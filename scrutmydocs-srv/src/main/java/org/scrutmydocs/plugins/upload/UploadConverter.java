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

import org.scrutmydocs.converters.IdGeneratorService;
import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.exceptions.SMDException;
import org.scrutmydocs.exceptions.SMDExtractionException;
import org.scrutmydocs.plugins.tika.TikaConverter;
import org.scrutmydocs.plugins.tika.TikaService;
import org.scrutmydocs.services.SMDConfigurationService;
import restx.factory.Component;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Component
public class UploadConverter extends TikaConverter<byte[]> {

    @Inject
    public UploadConverter(TikaService tikaService,
                           SMDConfigurationService smdConfigurationService,
                           IdGeneratorService idGeneratorService) {
        super(tikaService, smdConfigurationService, idGeneratorService);
    }

    @Override
    public SMDDocument toDocument(byte[] source) throws SMDException {
        logger.debug("generating SMDDocument from binary content");
        try (InputStream is = new ByteArrayInputStream(source)) {
            return toDocument(
                    is,
                    UUID.randomUUID().toString(),
                    UploadPlugin.TYPE_UPLOAD,
                    null,
                    null,
                    null,
                    new Date(),
                    null,
                    new Long(source.length)
            );
        } catch (IOException e) {
            throw new SMDExtractionException(e);
        }
    }
}
