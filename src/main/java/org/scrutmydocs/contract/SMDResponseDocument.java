package org.scrutmydocs.contract;

import java.util.Collection;

public class SMDResponseDocument {

	public final String name;
	public final String url;
	public final String contentType;
	public final Collection<String> highlights;

	public SMDResponseDocument(String name, String url, String contentType,
			Collection<String> highlights) {
		if (name == null) {
			throw new IllegalArgumentException(
					"name can't be null for a SMDResponseDocument");
		}
		this.name = name;
		this.url = url;
		this.contentType = contentType;
		this.highlights = highlights;
	}
}
