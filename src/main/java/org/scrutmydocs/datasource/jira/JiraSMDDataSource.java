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
package org.scrutmydocs.datasource.jira;

import java.util.Date;
import java.util.List;

import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.datasource.SMDDataSource;
import org.scrutmydocs.datasource.SMDRegister;
import org.scrutmydocs.webapp.util.StringTools;

/**
 * Manage Jira Rivers metadata
 * 
 * @author Johann NG SING KWONG
 * 
 */
@SMDRegister(name = "jira")
public class JiraSMDDataSource extends SMDDataSource {

	private static final long serialVersionUID = 1L;
	private String urlBase;
	private String username;
	private String pwd;
	private String jqlTimeZone;
	private String timeout;
	private Long maxIssuesPerRequest;
	private String projectKeysIndexed;
	private String indexUpdatePeriod;
	private String indexFullUpdatePeriod;
	private Long maxIndexingThreads;
	private String analyzer;
	private String jiraIssueCommentType;
	private String jiraRiverUpdateType;
	private String jiraRiverActivityIndexName;

	public JiraSMDDataSource(String id, String url, Long updateRate,
			String username, String pwd, String jqlTimeZone, String timeout,
			Long maxIssuesPerRequest, String projectKeysIndexed,
			String indexUpdatePeriod, String indexFullUpdatePeriod,
			Long maxIndexingThreads, String analyzer,String jiraIssueCommentType,String jiraRiverUpdateType,String jiraRiverActivityIndexName) {
		super(id, url, updateRate);
		this.username = username;
		this.pwd = pwd;
		this.jqlTimeZone = jqlTimeZone;
		this.timeout = timeout;
		this.maxIssuesPerRequest = maxIssuesPerRequest;
		this.projectKeysIndexed = projectKeysIndexed;
		this.indexUpdatePeriod = indexUpdatePeriod;
		this.indexFullUpdatePeriod = indexFullUpdatePeriod;
		this.maxIndexingThreads = maxIndexingThreads;
		this.analyzer = analyzer;
		this.jiraIssueCommentType = jiraIssueCommentType;
		this.jiraRiverUpdateType = jiraRiverUpdateType;
		this.jiraRiverActivityIndexName = jiraRiverActivityIndexName;
	}

	public JiraSMDDataSource() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return base URL of JIRA instance
	 */
	public String getUrlBase() {
		return urlBase;
	}

	/**
	 * @param urlBase
	 *            base URL of JIRA instance
	 */
	public void setUrlBase(String urlBase) {
		this.urlBase = urlBase;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getJqlTimeZone() {
		return jqlTimeZone;
	}

	public void setJqlTimeZone(String jqlTimeZone) {
		this.jqlTimeZone = jqlTimeZone;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public Long getMaxIssuesPerRequest() {
		return maxIssuesPerRequest;
	}

	public void setMaxIssuesPerRequest(Long maxIssuesPerRequest) {
		this.maxIssuesPerRequest = maxIssuesPerRequest;
	}

	public String getProjectKeysIndexed() {
		return projectKeysIndexed;
	}

	public void setProjectKeysIndexed(String projectKeysIndexed) {
		this.projectKeysIndexed = projectKeysIndexed;
	}

	public String getIndexUpdatePeriod() {
		return indexUpdatePeriod;
	}

	public void setIndexUpdatePeriod(String indexUpdatePeriod) {
		this.indexUpdatePeriod = indexUpdatePeriod;
	}

	public String getIndexFullUpdatePeriod() {
		return indexFullUpdatePeriod;
	}

	public void setIndexFullUpdatePeriod(String indexFullUpdatePeriod) {
		this.indexFullUpdatePeriod = indexFullUpdatePeriod;
	}

	public Long getMaxIndexingThreads() {
		return maxIndexingThreads;
	}

	public void setMaxIndexingThreads(Long maxIndexingThreads) {
		this.maxIndexingThreads = maxIndexingThreads;
	}

	public String getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(String analyzer) {
		this.analyzer = analyzer;
	}

	public String getJiraIssueCommentType() {
		return jiraIssueCommentType;
	}

	public void setJiraIssueCommentType(String jira_issue_comment) {
		this.jiraIssueCommentType = jira_issue_comment;
	}

	public String getJiraRiverActivityIndexName() {
		return jiraRiverActivityIndexName;
	}

	public void setJiraRiverActivityIndexName(String jiraRiverActivityIndex) {
		this.jiraRiverActivityIndexName = jiraRiverActivityIndex;
	}

	public String getJiraRiverUpdateType() {
		return jiraRiverUpdateType;
	}

	public void setJiraRiverUpdateType(String jiraRiverUpdate) {
		this.jiraRiverUpdateType = jiraRiverUpdate;
	}

	@Override
	public String toString() {
		return StringTools.toString(this);
	}

	@Override
	public List<SMDDocument> changes(Date since) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentPath(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SMDDocument getDocument(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}
}
