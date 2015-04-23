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

import org.scrutmydocs.domain.SMDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import restx.factory.Component;

import javax.inject.Inject;

@Component
public class JsonToSMDDocumentService {
    private static final Logger logger = LoggerFactory.getLogger(JsonToSMDDocumentService.class);

    private JsonService jsonService;

    @Inject
    public JsonToSMDDocumentService(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    public SMDDocument toDocument(String json)  {
        try {
            SMDDocument smdDocument = jsonService.mapper().readValue(json, SMDDocument.class);
            return smdDocument;
        } catch (Exception e) {
            logger.warn("Can not parse json document to SMDDocument: [{}]", json);
            throw new RuntimeException("Can not parse json document", e);
        }
    }
}
