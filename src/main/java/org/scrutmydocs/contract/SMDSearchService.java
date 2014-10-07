package org.scrutmydocs.contract;


public interface SMDSearchService {

	public void index(SMDFileDocument smdDocument);

	public SMDSearchResponse search(String search, int first, int pageSize);

	public void deleteAllDocumentsInDirectory(String directory);

	public SMDFileDocument getDocument(String id);

}
