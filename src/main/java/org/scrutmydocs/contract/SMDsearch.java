package org.scrutmydocs.contract;

import java.util.List;

import org.scrutmydocs.datasource.SMDDataSource;

public interface SMDsearch {

	public void index(SMDDataSource smdDataSource, SMDDocument smdDocument);

	public SMDSearchResponse search(String search, int first, int pageSize);

	public void saveSetting(SMDDataSource smdDataSource);

	public List<SMDDataSource> getSettings(SMDDataSource smdDataSource);

	public SMDDataSource getSetting(SMDDataSource smdDataSource, String id);

	void deleteSetting(SMDDataSource smdDataSource, String id);

	public void delete(String id);

}
