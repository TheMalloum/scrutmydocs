package org.scrutmydocs.plugins;

import org.junit.Test;
import org.scrutmydocs.ScrutMyDocsTests;
import org.scrutmydocs.plugins.fs.FileSystemConverter;
import org.scrutmydocs.services.SMDDocumentService;
import org.scrutmydocs.services.SMDRepositoriesService;
import org.scrutmydocs.domain.SMDRepository;
import org.scrutmydocs.plugins.fs.FSSMDRepository;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class TestRepositories extends ScrutMyDocsTests {

    @Inject
    public TestRepositories(SMDRepositoriesService repositoriesService,
                            SMDDocumentService searchService,
                            FileSystemConverter fileSystemConverter) {
        super(repositoriesService, searchService, fileSystemConverter);
    }

    @Test
	public void testIndexRepository() throws Exception {
		FSSMDRepository repository = new FSSMDRepository();
        repository.id = "test-fs";

        repositoriesService.save(repository);

        List<SMDRepository> repositories = repositoriesService.getRepositories();

        assertThat(repositories.size(), is(1));

        SMDRepository plugin = repositories.get(0);
        assertThat(plugin.type, is(repository.type));
        // TODO Fix that. We send a JSON which has id. We get it back but it's not deserialized
        assertThat(plugin.id, notNullValue());
        assertThat(plugin.id, is(repository.id));
        assertThat(plugin.type, is(repository.type));
        assertThat(plugin.url, is(repository.url));
	}
}
