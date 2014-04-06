package org.scrutmydocs.contract;

import java.util.List;

import org.scrutmydocs.datasource.SMDDataSource;

public interface SMDsearch {

	public void index(SMDDataSource smdDataSource, SMDDocument smdDocument);

	public void delete(String id);

	public SMDSearchResponse search(String search, int first, int pageSize);

	public void saveConf(SMDDataSource smdDataSource);

	public List<SMDDataSource> getConf(SMDDataSource smdDataSource);

}
