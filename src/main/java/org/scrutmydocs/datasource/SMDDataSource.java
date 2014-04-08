package org.scrutmydocs.datasource;

import java.util.Date;
import java.util.List;

import org.scrutmydocs.contract.SMDDocument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class SMDDataSource {

	public SMDDataSource(String json) {
	}

	public SMDDataSource(String id, String url, Long updateRate) {
		this.id = id;
		this.updateRate = updateRate;
		this.url = url;
		this.analyzer = "standard";
	}

	public SMDDataSource() {
	}

	public String name() {
		SMDRegister myRegister = this.getClass().getAnnotation(
				SMDRegister.class);
		return myRegister.name();

	}

	public String id;

	public Long updateRate;

	public String includes;

	public String excludes;

	public String analyzer;

	public String url;

	public Date date;

	public boolean start;

	public abstract List<SMDDocument> changes(Date since);

	public abstract String getDocumentPath(String id);

	public abstract SMDDocument getDocument(String id);

	public abstract String toJson();

}
