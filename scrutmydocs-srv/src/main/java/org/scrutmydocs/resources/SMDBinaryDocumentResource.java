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

package org.scrutmydocs.resources;

import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.services.SMDDocumentService;
import org.scrutmydocs.exceptions.SMDException;
import org.scrutmydocs.services.SMDRepositoryScanReflectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.*;
import restx.factory.Component;
import restx.http.HttpStatus;

import javax.inject.Inject;
import java.io.IOException;

@Component
public class SMDBinaryDocumentResource extends StdRoute {
    private static final Logger logger = LoggerFactory.getLogger(SMDBinaryDocumentResource.class);
    private SMDDocumentService searchService;
    private SMDRepositoryScanReflectionService repositoryScanReflectionService;

    @Inject
    public SMDBinaryDocumentResource(SMDDocumentService searchService,
                                     SMDRepositoryScanReflectionService repositoryScanReflectionService) {
        super("SMDBinaryDocumentResource", new StdRestxRequestMatcher("GET", ScrutmydocsApi.API_ROOT_DOCUMENT + "/{id}"));
        this.searchService = searchService;
        this.repositoryScanReflectionService = repositoryScanReflectionService;
    }

    @Override
    public void handle(RestxRequestMatch match, RestxRequest req, RestxResponse resp, RestxContext ctx) throws IOException {

        String id = match.getPathParam("id");
        logger.debug("GET id [{}]", id);

        if (id == null) {
            resp.setStatus(HttpStatus.BAD_REQUEST);
            return;
        }

        try {
            SMDDocument smdDocument = searchService.getDocument(id);
            if (smdDocument == null) {
                resp.setStatus(HttpStatus.NOT_FOUND);
                return;
            }

            byte[] binaryDoc = repositoryScanReflectionService.getListScan(
                    smdDocument.type).get(
                    smdDocument);

            // We set the filename
            resp.setHeader("Content-Disposition", "attachment; filename=" + smdDocument.file.filename);

            // We set the content type
            resp.setContentType(smdDocument.file.content_type);

            // We write the document as a stream
            resp.getOutputStream().write(binaryDoc);
        } catch (SMDException e) {
            resp.setStatus(HttpStatus.BAD_REQUEST);
            return;
        }

    }
}
