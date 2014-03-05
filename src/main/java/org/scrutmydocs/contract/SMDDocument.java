package org.scrutmydocs.contract;


public class SMDDocument {

	public final String id;
	public final String name;
	public final String contentType;
	public final byte[] content;
	
	public SMDDocument(String id, String name, String contentType,
			byte[] content) {
		super();
		this.id = id;
		this.name = name;
		this.contentType = contentType;
		this.content = content;
	}
	
	
	

	

}
