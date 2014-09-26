package org.scrutmydocs.contract;

import org.scrutmydocs.repositories.SMDRepositoryData;


public interface SMDSearchService {

	public void index(SMDRepositoryData smdAbstractPlugin, SMDFileDocument smdDocument);

    public void delete(SMDRepositoryData smdAbstractPlugin, String id);

	public SMDSearchResponse search(String search, int first, int pageSize);


	public SMDSearchResponse searchFileByDirectory(SMDRepositoryData smdAbstractPlugin, String directory, int first,
			int pageSize);

}
