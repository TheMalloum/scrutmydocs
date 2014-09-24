package org.scrutmydocs.contract;

import java.util.List;


public interface SMDRepositoriesService {

	public void save(SMDRepository repository);

	public List<SMDRepository>  getRepositories();

	public SMDRepository get(String id);
	
}
