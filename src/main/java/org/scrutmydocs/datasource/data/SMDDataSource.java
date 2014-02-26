package org.scrutmydocs.datasource.data;

import java.util.List;

import org.scrutmydocs.datasource.data.SMDChanges;
import org.scrutmydocs.documents.SMDDocument;

public abstract class SMDDataSource {

	public String getName() {
		return this.getClass().getName();
	}

	public abstract String getID();

	public abstract List<SMDChanges> changes(String since);

	public abstract String getDocumentPath(String id);
	
	public abstract SMDDocument getDocument(String id);

}
