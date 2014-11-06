package org.scrutmydocs.services;


import org.scrutmydocs.domain.SMDDocument;
import org.scrutmydocs.domain.SMDSearchQuery;
import org.scrutmydocs.domain.SMDSearchResponse;
import org.scrutmydocs.exceptions.SMDDocumentNotFoundException;
import org.scrutmydocs.exceptions.SMDIndexException;
import org.scrutmydocs.exceptions.SMDJsonParsingException;

public interface SMDDocumentService {

	public void index(SMDDocument smdDocument) throws SMDIndexException;

	public SMDSearchResponse search(SMDSearchQuery searchQuery) throws SMDJsonParsingException;

	public void deleteDirectory(String directory);

    public SMDDocument getDocument(String id) throws SMDDocumentNotFoundException, SMDJsonParsingException;

    public void deleteDocument(String id);
}
