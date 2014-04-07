package org.scrutmydocs.contract;

import java.util.List;

import org.scrutmydocs.datasource.SMDDataSource;
import org.scrutmydocs.datasource.drive.DriveSMDDataSource;

public interface SMDsearch {

	public void index(SMDDataSource smdDataSource, SMDDocument smdDocument);

	public SMDSearchResponse search(String search, int first, int pageSize);

	public void saveConf(SMDDataSource smdDataSource);

	public List<SMDDataSource> getConf(SMDDataSource smdDataSource);

	public SMDDataSource getConf(SMDDataSource smdDataSource, String id);

	void delelteConf(SMDDataSource smdDataSource, String id);

	public void delete(String id);

}
