package org.scrutmydocs.contract;

import java.util.Collection;

public class SMDDocument {

	public final String id;
	public final String name;
	public final String url;
	public final String contentType;
	public final String path;
	public final Collection<String> highlights;
	
	

	public SMDDocument(String name, String url, String contentType,String path,
			Collection<String> highlights) {
		if (name == null) {
			throw new IllegalArgumentException(
					"type can't be null for a SMDResponseDocument");
		}
		this.id = null;
		this.name = name;
		this.url = url;
		this.contentType = contentType;
		this.path=path;
		this.highlights = highlights;
		
	}

	public SMDDocument(String name, String url, String contentType,String path) {
		if (name == null) {
			throw new IllegalArgumentException(
					"type can't be null for a SMDResponseDocument");
		}
		this.id = null;
		this.name = name;
		this.url = url;
		this.contentType = contentType;
		this.path=path;
		this.highlights = null;
	}
}
