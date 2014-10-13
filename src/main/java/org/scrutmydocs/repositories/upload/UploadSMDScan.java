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

package org.scrutmydocs.repositories.upload;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scrutmydocs.annotations.SMDRegisterRepositoryScan;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.contract.SMDRepositoryData;
import org.scrutmydocs.contract.SMDRepositoryScan;

/**
 * Implement the DropBox ScrutMyDocs Data Source
 * 
 * @author Malloum LAYA
 * 
 */
@SMDRegisterRepositoryScan(name = "upload")
public class UploadSMDScan extends SMDRepositoryScan {

	protected Logger logger = LogManager.getLogger(getClass().getName());


	@Override
	public void scrut(SMDRepositoryData smdRepositoryData) {
	}


	@Override
	public boolean check(SMDRepositoryData smdRepositoryData) {
		return false;
	}


	@Override
	public byte[] get(SMDDocument document) {
		return null;
	}


	@Override
	public void delete(SMDRepositoryData smdRepositoryData) {
		
	}

}
