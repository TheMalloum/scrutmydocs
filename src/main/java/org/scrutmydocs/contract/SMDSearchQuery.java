package org.scrutmydocs.contract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SMDSearchQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	public String search;
	public int first;
	public int pageSize;
	public List<String> groups = new ArrayList<String>();

	public SMDSearchQuery() {
		super();
	}

	public SMDSearchQuery(String search, int first, int pageSize,
			List<String> groups) {
		this.search = search;
		this.first = first;
		this.pageSize = pageSize;
		this.groups = groups;
	}
	
//	public SMDSearchQuery(String search, int first, int pageSize) {
//		this.search = search;
//		this.first = first;
//		this.pageSize = pageSize;
//		 groups.add(Group.ANONYME.name());
//	}

}
