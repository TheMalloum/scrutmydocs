package org.scrutmydocs.contract;

import java.util.List;
import java.util.Map;

public interface SMDsearch {

	public void index(SMDDocument smdDocument);

	public void delete(String id);

	public SMDSearchResponse search(String search, int first, int pageSize);
	
	public void saveAdmin(SMDDataSource dataSource);

	public List<SMDDataSource> getConf();

}
