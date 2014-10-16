package org.scrutmydocs.contract;

import org.scrutmydocs.search.SMDSearchFactory;

import java.util.List;

public abstract class SMDRepositoriesService {

	public abstract void save(SMDRepositoryData repository);

	public abstract List<SMDRepositoryData> getRepositories();

	public abstract SMDRepositoryData get(String id);

	protected abstract void deleteRepositorySetting(SMDRepositoryData repository);

	public void deleteRepository(SMDRepositoryData repository) {

		SMDSearchFactory.getInstance().deleteDirectory(repository.url);
		deleteRepositorySetting(repository);

	}
}
