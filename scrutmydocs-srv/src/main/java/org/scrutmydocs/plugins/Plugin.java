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

/**
 * You can implement this interface if you want to provide plugin
 * which will generate documents from your data source
 */
public interface Plugin<T> {

    /**
     * Generate a Document from object of type T
     */
    public SMDDocument toDocument(T source) throws SMDException;

    /**
     * Supported type
     */
    public String type();

    /**
     * Plugin name
     */
    public String name();

    /**
     * Plugin version
     */
    public String version();
}
