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

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.scrutmydocs.exceptions.SMDException;
import org.scrutmydocs.exceptions.SMDIllegalArgumentException;
import org.scrutmydocs.jobs.PluginScrutinerJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.AutoStartable;
import restx.factory.Component;
import restx.factory.Factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class registers plugins and starts them
 */
@Component
public class PluginService implements AutoStartable {
    private static final Logger logger = LoggerFactory.getLogger(PluginService.class);

    private final Map<String, Plugin<?,?,?>> plugins;

    public PluginService() {
        plugins = new HashMap<>();
    }

    public void registerPlugins() {
        // TODO We need to find the right factory name!
        Factory factory = Factory.getInstance();
        Set<Plugin> components = factory.getComponents(Plugin.class);
        for (Plugin plugin : components) {
            // Register all plugins
            registerPlugin(plugin);
        }
    }

    public void registerPlugin(Plugin plugin) {
        plugins.put(plugin.type(), plugin);
    }

    public Plugin getPlugin(String type) throws SMDIllegalArgumentException {
        if (plugins.containsKey(type)) {
            return plugins.get(type);
        }

        throw new SMDIllegalArgumentException("plugin [" + type + "] has not been registered.");
    }

    public void start() {
        try {
            registerPlugins();
            for (String pluginType : plugins.keySet()) {
                logger.debug("starting plugin [{}]", pluginType);
                plugins.get(pluginType).start();
            }

            // We register a job
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("plugins", plugins);
            JobDetail job = JobBuilder.newJob(PluginScrutinerJob.class)
                    .withIdentity("scrutiner-jobs")
                    .usingJobData(jobDataMap).build();

            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInSeconds(10)
                                            //.withIntervalInMinutes(1)
                                    .repeatForever())
                    .build();

            // schedule the job
            StdSchedulerFactory.getDefaultScheduler().scheduleJob(job, trigger);
            StdSchedulerFactory.getDefaultScheduler().start();
        } catch (SMDException e) {
            logger.error("can not register plugins...", e);
        } catch (SchedulerException e) {
            logger.error("can not start jobs...", e);
        }
    }
}
