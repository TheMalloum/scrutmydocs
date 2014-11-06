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

package org.scrutmydocs.services;

import org.reflections.Reflections;
import org.scrutmydocs.annotations.SMDRegisterRepositoryScan;
import org.scrutmydocs.domain.SMDRepositoryScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Component;

import java.util.HashMap;
import java.util.Set;


@Component
public class SMDRepositoryScanReflectionService {

    private static final Logger logger = LoggerFactory.getLogger(SMDRepositoryScanReflectionService.class);

    private final HashMap<String, SMDRepositoryScan> listScan;

    public SMDRepositoryScanReflectionService() {
        Reflections reflections = new Reflections("org.scrutmydocs");

        listScan = new HashMap<>();

        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(SMDRegisterRepositoryScan.class);

        for (Class<?> annotatedScan : annotated) {
            if (SMDRepositoryScan.class.isAssignableFrom(annotatedScan)) {
                if (listScan.get(annotatedScan) != null) {
                    logger.error("[{}] SMDRepositoryScan is already registered. Ignoring.",
                            listScan.get(annotatedScan.getName()));
                } else {
                    try {
                        listScan.put(annotatedScan.getAnnotation(SMDRegisterRepositoryScan.class).name(),
                                (SMDRepositoryScan) annotatedScan.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        logger.error("Can not instantiate [{}] SMDRepositoryScan. Ignoring.",
                                listScan.get(annotatedScan.getName()));
                    }
                }
            } else {
                logger.warn("[{}] must extend [{}]. Skipping.",
                        annotatedScan.getName(), SMDRepositoryScan.class.getName());
            }
        }
    }

    public HashMap<String, SMDRepositoryScan> getListScan() {
        return listScan;
    }

    public SMDRepositoryScan getListScan(String type) {
        return listScan.get(type);
    }
}
