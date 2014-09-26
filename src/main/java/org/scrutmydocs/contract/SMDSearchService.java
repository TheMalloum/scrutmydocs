package org.scrutmydocs.contract;


public interface SMDSearchService {

	public void index(SMDRepository smdAbstractPlugin, SMDFileDocument smdDocument);

    public void delete(SMDRepository smdAbstractPlugin, String id);

	public SMDSearchResponse search(String search, int first, int pageSize);


	public SMDSearchResponse searchFileByDirectory(SMDRepository smdAbstractPlugin, String directory, int first,
			int pageSize);

}
