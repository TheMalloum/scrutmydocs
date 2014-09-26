package org.scrutmydocs.contract;

import java.util.List;

import org.scrutmydocs.repositories.SMDRepositoryScan;
import org.scrutmydocs.repositories.SMDRepositoryData;
import org.scrutmydocs.repositories.fs.FSSMDRepositoryData;


public interface SMDRepositoriesService {

	public void save(SMDRepositoryData repository);

	public List<SMDRepositoryData>  getRepositories();

	public SMDRepositoryData get(String id);
	
}
