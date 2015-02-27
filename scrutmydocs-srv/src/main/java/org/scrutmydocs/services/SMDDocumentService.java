package org.scrutmydocs.services;


import org.elasticsearch.action.search.SearchResponse;
import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.domain.SMDSearchQuery;
import org.scrutmydocs.exceptions.SMDDocumentNotFoundException;
import org.scrutmydocs.exceptions.SMDIndexException;
import org.scrutmydocs.exceptions.SMDJsonParsingException;

public interface SMDDocumentService {

	public void index(SMDDocument smdDocument) throws SMDIndexException;

	public SearchResponse search(SMDSearchQuery searchQuery) throws SMDJsonParsingException;

	public void deleteDirectory(String directory);

    public SMDDocument getDocument(String id) throws SMDDocumentNotFoundException, SMDJsonParsingException;

    public void deleteDocument(String id);
}
