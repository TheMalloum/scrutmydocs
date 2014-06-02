package org.scrutmydocs.contract;

import org.scrutmydocs.plugins.SMDAbstractPlugin;

public interface SMDSearchService {

	public void index(SMDAbstractPlugin smdAbstractPlugin, SMDDocument smdDocument);

    public void delete(SMDAbstractPlugin smdAbstractPlugin, String id);

	public SMDSearchResponse search(String search, int first, int pageSize);




}
