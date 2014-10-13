package org.scrutmydocs.contract;

import java.util.Collection;
import java.util.UUID;

public class SMDDocument {

	public String id;
	public String name;
	public String url;
	public String contentType;
	public String type;
	public Collection<String> highlights;
	public String pathDirectory;

	public SMDDocument() {
	}

	public SMDDocument(String id, String name, String url, String contentType,
			String type, String pathDirectory, Collection<String> highlights) {
		if (name == null) {
			throw new IllegalArgumentException(
					"name can't be null for a SMDResponseDocument");
		}

		if (type == null) {
			throw new IllegalArgumentException(
					"type can't be null for a SMDResponseDocument");
		}

		if (id == null) {
			throw new IllegalArgumentException(
					"id can't be null for a SMDResponseDocument");
		}

		this.id = id;
		this.name = name;
		this.url = url;
		this.contentType = contentType;
		this.type = type;
		this.pathDirectory = pathDirectory;
		this.highlights = highlights;

	}

	public SMDDocument(String name, String url, String contentType,
			String type, String pathDirectory) {
		if (name == null) {
			throw new IllegalArgumentException(
					"name can't be null for a SMDResponseDocument");
		}
		if (type == null) {
			throw new IllegalArgumentException(
					"type can't be null for a SMDResponseDocument");
		}
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.url = url;
		this.contentType = contentType;
		this.type = type;
		this.pathDirectory = pathDirectory;
		this.highlights = null;
	}

}
