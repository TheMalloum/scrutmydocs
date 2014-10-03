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

package org.scrutmydocs.repositories.fs;

import org.scrutmydocs.repositories.SMDRepositoryData;
import org.scrutmydocs.repositories.annotations.SMDRegisterRepositoryData;

import com.sun.istack.NotNull;

/**
 * Implement the DropBox ScrutMyDocs Data Source
 * 
 * @author Malloum LAYA
 * 
 */
@SMDRegisterRepositoryData(name = "fs")
public class FSSMDRepositoryData extends SMDRepositoryData {

	public FSSMDRepositoryData(String absolutePath) {
		this.url = absolutePath;
		this.type="fs";
	}

	public FSSMDRepositoryData() {
		this.type="fs";
	}


	@NotNull
	public String url;

	public String includes;

	public String excludes;

	public String analyzer;

}
