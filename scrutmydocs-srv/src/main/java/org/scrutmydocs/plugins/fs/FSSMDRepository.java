/*
 * Licensed to scrutmydocs.org (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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

import org.scrutmydocs.annotations.SMDRegisterRepository;
import org.scrutmydocs.domain.SMDRepository;

/**
 * Implements the FS ScrutMyDocs Data Source
 * 
 * @author Malloum LAYA
 * 
 */
@SMDRegisterRepository(name = FSSMDRepository.TYPE)
public class FSSMDRepository extends SMDRepository {

    public static final String TYPE = "fs";

	public FSSMDRepository(String absolutePath) {
		this.url = absolutePath;
		this.type = TYPE;
	}

	public FSSMDRepository() {
		this.type = TYPE;
	}

	public String includes;

	public String excludes;

	public String analyzer;
}
