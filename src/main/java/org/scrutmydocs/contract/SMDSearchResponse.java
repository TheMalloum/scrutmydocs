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

package org.scrutmydocs.contract;

import java.util.ArrayList;
import java.util.List;

public class SMDSearchResponse  {
	protected long took;
	protected long totalHits;
	protected List<SMDDocument> smdDocuments = new ArrayList<SMDDocument>();

	
	/**
	 * @param took
	 * @param totalHits
	 * @param hits
	 */
	public SMDSearchResponse(long took, long totalHits, List<SMDDocument> smdDocuments) {
		super();
		this.took = took;
		this.totalHits = totalHits;
		this.smdDocuments = smdDocuments;
	}

	/**
	 * @return the took
	 */
	public long getTook() {
		return took;
	}

	/**
	 * @param took
	 *            the took to set
	 */
	public void setTook(long took) {
		this.took = took;
	}

	/**
	 * @return the totalHits
	 */
	public long getTotalHits() {
		return totalHits;
	}

	/**
	 * @param totalHits
	 *            the totalHits to set
	 */
	public void setTotalHits(long totalHits) {
		this.totalHits = totalHits;
	}


	public List<SMDDocument> getSmdDocuments() {
		return smdDocuments;
	}


	public void setSmdDocuments(List<SMDDocument> smdDocuments) {
		this.smdDocuments = smdDocuments;
	}

	

}
