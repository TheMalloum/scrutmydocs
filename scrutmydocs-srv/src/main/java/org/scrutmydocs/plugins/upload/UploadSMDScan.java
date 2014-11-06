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

package org.scrutmydocs.plugins.upload;

import org.scrutmydocs.annotations.SMDRegisterRepositoryScan;
import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.domain.SMDRepository;
import org.scrutmydocs.domain.SMDRepositoryScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement the DropBox ScrutMyDocs Data Source
 * 
 * @author Malloum LAYA
 * 
 */
@SMDRegisterRepositoryScan(name = "upload")
public class UploadSMDScan extends SMDRepositoryScan {

    private static final Logger logger = LoggerFactory.getLogger(UploadSMDScan.class);

	@Override
	public void scrut(SMDRepository smdRepository) {
	}


	@Override
	public boolean check(SMDRepository smdRepository) {
		return false;
	}


	@Override
	public byte[] get(SMDDocument document) {
		return null;
	}


	@Override
	public void delete(SMDRepository smdRepository) {
		
	}

}
