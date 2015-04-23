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

package org.scrutmydocs.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * You can extend this class if you want to provide plugin
 * which will generate documents from your data source
 */
public abstract class Plugin<T, L extends DocumentListener<T>, R extends Runner> {
    protected static final Logger logger = LoggerFactory.getLogger(Plugin.class);

    private boolean started = false;
    protected L documentListener = null;
    protected R runner = null;

    public L getDocumentListener() {
        return documentListener;
    }

    public R getRunner() {
        return runner;
    }

    /**
     * Supported type
     */
    public abstract String type();

    /**
     * Plugin name
     */
    public abstract String name();

    /**
     * Plugin version
     */
    public abstract String version();

    /**
     * Start the plugin
     */
    public void start()  {
        logger.debug("starting plugin [{}]/[{}]", name(), version());

        if (runner != null) {
            // If we have a runner, we simply use it
            runner.run();
        } else {
            // TODO Add the plugin to the Jobs
        }

        started = true;
    }

    /**
     * Stop the plugin
     */
    public void stop()  {
        logger.debug("stopping plugin [{}]/[{}]", name(), version());
        started = false;
    }

    public boolean isStarted() {
        return started;
    }

}
