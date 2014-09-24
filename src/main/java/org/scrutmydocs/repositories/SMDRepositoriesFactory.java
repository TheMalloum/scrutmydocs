package org.scrutmydocs.repositories;

import org.scrutmydocs.contract.SMDRepositoriesService;
import org.scrutmydocs.search.ElasticSearchImpl;

public class SMDRepositoriesFactory {

	private static SMDRepositoriesService repositoriesService;

    /**
     * We instantiate an elasticsearch backend implementation
     */
	public static synchronized SMDRepositoriesService getInstance() {
		if (repositoriesService == null) {
			repositoriesService = new ElasticSearchImpl();
		}

		return repositoriesService;
	}
	
}
