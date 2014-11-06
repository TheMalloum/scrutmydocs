package org.scrutmydocs.services;

import org.scrutmydocs.domain.SMDRepository;
import org.scrutmydocs.exceptions.SMDIllegalArgumentException;
import org.scrutmydocs.exceptions.SMDJsonParsingException;

import java.util.List;

public interface SMDRepositoriesService {

	public void save(SMDRepository repository);

	public List<SMDRepository> getRepositories();

	public SMDRepository get(String id) throws SMDIllegalArgumentException, SMDJsonParsingException;

	public void deleteRepositorySetting(SMDRepository repository) throws SMDIllegalArgumentException;

	public void deleteRepository(SMDRepository repository) throws SMDIllegalArgumentException;
}
