package org.scrutmydocs.contract;

import java.io.Serializable;

public class SMDSearchQuery implements Serializable {
	private static final long serialVersionUID = 1L;

	public String search;
	public int first;
	public int pageSize;

}
