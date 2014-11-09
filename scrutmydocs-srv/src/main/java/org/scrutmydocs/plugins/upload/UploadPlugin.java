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

import org.apache.commons.codec.binary.Base64;
import org.scrutmydocs.annotations.SMDRegisterRepository;
import org.scrutmydocs.plugins.AbstractTikaPlugin;
import org.scrutmydocs.plugins.dummy.DummyDocumentListener;
import restx.factory.Component;

import javax.inject.Inject;

/**
 * This plugin processes an uploaded stream
 */
@Component
@SMDRegisterRepository(name = UploadPlugin.TYPE_UPLOAD)
public class UploadPlugin
        extends AbstractTikaPlugin<byte[], DummyDocumentListener<byte[]>, UploadRunner> {

    public static final String TYPE_UPLOAD = "upload";

    /**
     * The file system plugin uses a listener and no runner
     */
    @Inject
    public UploadPlugin(UploadRunner uploadRunner) {
        runner = uploadRunner;

        // TODO For test purpose. Remove it!
        uploadRunner.setData(Base64.decodeBase64("SGVsbG8gV29ybGQK"));
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
