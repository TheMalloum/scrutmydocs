package org.scrutmydocs.contract;

import java.util.Map;

public interface SMDsearch {

	public void index(SMDDocument smdDocument);

	public void delete(String id);

	public SMDSearchResponse search(String search, int first, int pageSize);

	
	
	// if we decide to save the configuration in the same place that the indexes, this choice could be done if we want us the clusters capacity of the search.
	
	public void saveAdmin(Map<String, String> json);

	public Map<String, String> getAdmin();

}
