package org.scrutmydocs.services;


import org.elasticsearch.action.search.SearchResponse;
import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.domain.SMDSearchQuery;
import org.scrutmydocs.exceptions.SMDIndexException;

public interface SMDDocumentService {

	public void index(SMDDocument smdDocument) throws SMDIndexException;

	public SearchResponse search(SMDSearchQuery searchQuery) ;

	public void deleteDirectory(String directory);

    public SMDDocument getDocument(String id);

    public void deleteDocument(String id);
}
