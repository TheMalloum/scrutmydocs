package org.scrutmydocs.contract;

import java.util.Date;


public class SMDDocument {

	public final String id;
	public final String name;
	public final String contentType;
	public final byte[] content;
	public final Date date;
	
	public SMDDocument(String id, String name, String contentType,
			byte[] content,Date date) {
		super();
		this.id = id;
		this.name = name;
		this.contentType = contentType;
		this.content = content;
		this.date=date;
	}
	
	
	

	

}
