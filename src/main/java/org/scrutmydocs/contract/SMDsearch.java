package org.scrutmydocs.contract;

import org.scrutmydocs.datasource.SMDDataSource;


public interface SMDsearch {

	public void index(SMDDocument smdDocument);

	public void delete(String id);

	public SMDSearchResponse search(String search, int first, int pageSize);
	
	public void saveConf();

	public SMDDataSource getConf();

}
