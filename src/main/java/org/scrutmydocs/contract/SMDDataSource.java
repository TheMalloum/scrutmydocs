package org.scrutmydocs.contract;

import java.util.List;
import java.util.Map;

public abstract class SMDDataSource {

	public SMDDataSource(Map<String, String> json){
	}
	
	public abstract String getName();
	
	public abstract String getID();
	
	public abstract List<SMDChanges> changes(String since);

	public abstract String getDocumentPath(String id);
	
	public abstract SMDDocument getDocument(String id);
	
	public abstract String checkSince();
	
	public abstract String updateSince(String since);
	

}
