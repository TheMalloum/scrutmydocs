package org.scrutmydocs.contract;

import java.util.List;

import org.scrutmydocs.repositories.SMDAbstractRepository;

public interface SMDRepositoriesService {

	public void save(SMDAbstractRepository repository);

	public List<SMDAbstractRepository>  getRepositories();

	public SMDAbstractRepository get(String id);
	
}
