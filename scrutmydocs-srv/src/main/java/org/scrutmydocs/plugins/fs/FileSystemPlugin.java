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

import org.joda.time.DateTime;
import org.scrutmydocs.annotations.SMDRegisterRepository;
import org.scrutmydocs.plugins.AbstractTikaPlugin;
import org.scrutmydocs.plugins.dummy.DummyRunner;
import restx.factory.Component;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Paths;

/**
 * This plugin processes a local file
 */
@Component
@SMDRegisterRepository(name = FileSystemPlugin.TYPE_FS)
public class FileSystemPlugin extends AbstractTikaPlugin<File, FileSystemDocumentListener, DummyRunner> {

    public static final String TYPE_FS = "fs";

    /**
     * The file system plugin uses a listener and no runner
     */
    @Inject
    public FileSystemPlugin(FileSystemDocumentListener listener) {
        documentListener = listener;
        documentListener.setLastChecked(DateTime.parse("1970-01-01"));
        documentListener.add(Paths.get("/Users/dpilato/Documents/Elasticsearch/tmp/es"));
        documentListener.add(Paths.get("/tmp/es"));
    }

    @Override
    public String type() {
        return TYPE_FS;
    }

    @Override
    public String name() {
        return "File System Plugin";
    }

    @Override
    public String version() {
        // Todo inject current version number
        return "N/A";
    }
}
