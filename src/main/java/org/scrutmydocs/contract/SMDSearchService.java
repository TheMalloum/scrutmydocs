package org.scrutmydocs.contract;

import org.scrutmydocs.repositories.SMDAbstractRepository;

public interface SMDSearchService {

	public void index(SMDAbstractRepository smdAbstractPlugin, SMDDocument smdDocument);

    public void delete(SMDAbstractRepository smdAbstractPlugin, String id);

	public SMDSearchResponse search(String search, int first, int pageSize);


	public SMDSearchResponse searchFileByDirectory(SMDAbstractRepository smdAbstractPlugin, String directory, int first,
			int pageSize);

}
