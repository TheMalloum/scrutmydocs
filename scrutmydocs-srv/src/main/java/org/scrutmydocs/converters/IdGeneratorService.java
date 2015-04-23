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

import org.elasticsearch.common.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import restx.factory.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Component
public class IdGeneratorService {
    private static final Logger logger = LoggerFactory.getLogger(IdGeneratorService.class);

    private MessageDigest sha;

    public IdGeneratorService() {
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Can not access to SHA-1 algorithm", e);
        }
    }

    public String  generateId(String type, String key)  {
        return Base64.encodeBytes(sha.digest(type.concat(key).getBytes()));
    }

}
