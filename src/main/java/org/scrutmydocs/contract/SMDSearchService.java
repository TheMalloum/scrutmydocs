package org.scrutmydocs.contract;

import org.scrutmydocs.repositories.SMDRepositoryData;

public interface SMDSearchService {

	public void index(SMDRepositoryData smdAbstractPlugin,
			SMDFileDocument smdDocument);

	public SMDSearchResponse search(String search, int first, int pageSize);

	public void deleteAllDocumentsInDirectory(
			SMDRepositoryData smdAbstractPlugin, String directory);

	public SMDFileDocument getDocument(String type, String id);

}
