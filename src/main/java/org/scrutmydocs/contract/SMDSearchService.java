package org.scrutmydocs.contract;


public interface SMDSearchService {

	public void index(SMDFileDocument smdDocument);

	public SMDSearchResponse search(SMDSearchQuery searchQuery);

	public void deleteDirectory(String directory);

	public SMDFileDocument getDocument(String id);

}
