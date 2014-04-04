package org.scrutmydocs.datasource;

import java.util.Date;
import java.util.List;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.scrutmydocs.contract.SMDChanges;
import org.scrutmydocs.contract.SMDDocument;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class SMDDataSource {

	protected ESLogger logger = Loggers.getLogger(getClass().getName());

	@JsonInclude
	public abstract String id();

	@JsonInclude
	public abstract Date since();

	
	@JsonIgnore
	public abstract List<SMDChanges> changes(Date since);

	@JsonIgnore
	public abstract String getDocumentPath(String id);

	@JsonIgnore
	public abstract SMDDocument getDocument(String id);

}
