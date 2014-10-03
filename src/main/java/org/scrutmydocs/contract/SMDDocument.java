package org.scrutmydocs.contract;

import java.util.Collection;

public class SMDDocument {

	public final String id;
	public final String name;
	public final String url;
	public final String contentType;
	public final Collection<String> highlights;

	public SMDDocument(String name, String url, String contentType,
			Collection<String> highlights) {
		if (name == null) {
			throw new IllegalArgumentException(
					"type can't be null for a SMDResponseDocument");
		}
		this.id = null;
		this.name = name;
		this.url = url;
		this.contentType = contentType;
		this.highlights = highlights;
	}

	public SMDDocument(String name, String url, String contentType) {
		if (name == null) {
			throw new IllegalArgumentException(
					"type can't be null for a SMDResponseDocument");
		}
		this.id = null;
		this.name = name;
		this.url = url;
		this.contentType = contentType;
		this.highlights = null;
	}
}
