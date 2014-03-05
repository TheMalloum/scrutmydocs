package org.scrutmydocs.contract;



public interface SMDsearch {
	

	public void index(SMDDocument smdDocument);

	public void delete(String id);

	public SMDSearchResponse search(String search, int first, int pageSize);

}
