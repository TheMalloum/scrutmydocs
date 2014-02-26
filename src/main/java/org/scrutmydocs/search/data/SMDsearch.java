package org.scrutmydocs.search.data;

import org.scrutmydocs.datasource.data.SMDDataSource;
import org.scrutmydocs.documents.SMDDocument;

public interface SMDsearch {

	public void indexe(SMDDocument smdDocument, SMDDataSource smdDataSource);

	
	public void delete(String id, SMDDataSource smdDataSource);


	public SMDSearchResponse search(String search, int first, int pageSize);

}
