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

package org.scrutmydocs.resources;

import org.elasticsearch.action.search.SearchResponse;
import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.domain.SMDSearchQuery;
import org.scrutmydocs.exceptions.SMDException;
import org.scrutmydocs.exceptions.SMDIndexException;
import org.scrutmydocs.exceptions.SMDJsonParsingException;
import org.scrutmydocs.plugins.upload.UploadConverter;
import org.scrutmydocs.services.SMDDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.annotations.DELETE;
import restx.annotations.POST;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;

import javax.inject.Inject;
import java.io.IOException;

@Component
@RestxResource(ScrutmydocsApi.API_ROOT_DOCUMENT)
public class SMDDocumentResource {

    private static final Logger logger = LoggerFactory.getLogger(SMDDocumentResource.class);
    private final SMDDocumentService documentService;
    private final UploadConverter uploadConverter;

    @Inject
    public SMDDocumentResource(SMDDocumentService documentService, UploadConverter uploadConverter) {
        this.documentService = documentService;
        this.uploadConverter = uploadConverter;
    }

    @POST("/_search")
    @PermitAll
    public SearchResponse search(SMDSearchQuery query) throws IOException {
        logger.debug("_search([{}])", query);
        try {
            return documentService.search(query);
        } catch (SMDJsonParsingException e) {
            // TODO Remove when Restx will be fixed - see https://github.com/restx/restx/issues/121
            throw new IOException(e);
        }
    }

    @POST("")
    @PermitAll
    public void addDocument(SMDDocument document) throws IOException {
        try {
            documentService.index(document);
        } catch (SMDIndexException e) {
            // TODO Remove when Restx will be fixed - see https://github.com/restx/restx/issues/121
            throw new IOException(e);
        }
    }

    // TODO implement it
    @POST("/_upload")
//    @Consumes("application/octet-stream")
    @PermitAll
    public void addBinaryDocument(byte[] data) throws IOException {
        // TODO replace with actual filename
        String filename = "dummy";
        try {
            SMDDocument document = uploadConverter.toDocument(data);
            documentService.index(document);
        } catch (SMDException e) {
            // TODO Remove when Restx will be fixed - see https://github.com/restx/restx/issues/121
            throw new IOException(e);
        }
    }

    @DELETE("/{id}")
    @PermitAll
    public void removeDocument(String id) {
        documentService.deleteDocument(id);
    }
}
