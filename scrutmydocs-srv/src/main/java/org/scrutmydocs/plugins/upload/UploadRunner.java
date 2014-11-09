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

import org.scrutmydocs.plugins.Runner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Component;

@Component
public class UploadRunner implements Runner {
    protected static final Logger logger = LoggerFactory.getLogger(UploadRunner.class);

    private byte[] data;

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public void run() {
        logger.debug("running uploader");
    }
}
