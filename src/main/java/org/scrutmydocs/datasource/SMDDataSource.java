package org.scrutmydocs.datasource;

import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import org.scrutmydocs.contract.SMDDocument;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class SMDDataSource {

	@JsonInclude
	@Nonnull
	public String name() {
		SMDRegister myRegister = this.getClass().getAnnotation(
				SMDRegister.class);
		return myRegister.name();

	}

	@JsonInclude
	@Nonnull
	public String id;

	@JsonInclude
	@Nonnull
	public String url;

	@JsonInclude
	@Nonnull
	public Date date;

	@JsonIgnore
	public abstract List<SMDDocument> changes(Date since);

	@JsonIgnore
	public abstract String getDocumentPath(String id);

	@JsonIgnore
	public abstract SMDDocument getDocument(String id);

}
