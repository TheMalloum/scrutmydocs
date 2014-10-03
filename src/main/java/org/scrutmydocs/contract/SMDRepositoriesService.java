package org.scrutmydocs.contract;

import java.util.List;

import org.scrutmydocs.repositories.SMDRepositoryData;


public interface SMDRepositoriesService {

	public void save(SMDRepositoryData repository);

	public List<SMDRepositoryData>  getRepositories();

	public SMDRepositoryData get(String id);

	public void deleteRepository(SMDRepositoryData repository);

}
