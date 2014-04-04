package org.scrutmydocs.datasource;

import java.util.Date;
import java.util.List;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.scrutmydocs.contract.SMDChanges;
import org.scrutmydocs.contract.SMDDocument;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class SMDDataSource {

	protected ESLogger logger = Loggers.getLogger(getClass().getName());

	@JsonInclude
	public String name() {
		SMDRegister myRegister = this.getClass().getAnnotation(
				SMDRegister.class);
		return myRegister.name();

	}

	@JsonInclude
	public String id;

	@JsonInclude
	public Date date;

	@JsonIgnore
	public abstract List<SMDChanges> changes(Date since);

	@JsonIgnore
	public abstract String getDocumentPath(String id);

	@JsonIgnore
	public abstract SMDDocument getDocument(String id);

}
