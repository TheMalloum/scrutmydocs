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

package org.scrutmydocs.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.scrutmydocs.exceptions.SMDException;
import org.scrutmydocs.plugins.DocumentListener;
import org.scrutmydocs.plugins.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PluginScrutinerJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(PluginScrutinerJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Do something
        logger.trace("JOB Running");

        JobDataMap jobDataMap = context.getMergedJobDataMap();
        Map<String, Plugin> plugins = (Map<String, Plugin>) jobDataMap.get("plugins");

        for (String pluginType : plugins.keySet()) {
            try {
                DocumentListener listener = plugins.get(pluginType).getDocumentListener();
                if (listener != null) {
                    logger.debug("executing plugin [{}]", pluginType);
                    for (Object o : listener.scrut()) {
                        listener.add(o);
                    }
                } else {
                    logger.trace("ignoring plugin [{}]: no listener implemented", pluginType);
                }
            } catch (SMDException e) {
                logger.warn("Something goes wrong while dealing with plugin [{}]: [{}]", pluginType, e.getMessage());
                logger.debug("error is:", e);
            }
        }

        logger.trace("JOB End");
    }
}
