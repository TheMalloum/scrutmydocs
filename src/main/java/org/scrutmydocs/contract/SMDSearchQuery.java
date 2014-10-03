package org.scrutmydocs.contract;

import java.io.Serializable;

public class SMDSearchQuery implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final String search;
	public final int first; 
	public final int pageSize;
	
	
	public SMDSearchQuery(String search, int first, int pageSize) {
		super();
		this.search = search;
		this.first = first;
		this.pageSize = pageSize;
	}
	
	

}
