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

import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.exceptions.SMDException;

import java.util.List;

/**
 * Implementing this interface means that the plugin will be called every x minutes
 * by a Job.
 */
public interface DocumentListener<T> {
    public List<String> scrut() throws SMDException;
    public SMDDocument get(String documentId) throws SMDException;
    public void delete(T document) throws SMDException;
}
