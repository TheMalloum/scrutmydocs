package org.scrutmydocs.repositories;

import org.junit.Test;
import org.scrutmydocs.ScrutMyDocsTests;
import org.scrutmydocs.contract.SMDRepositoryData;
import org.scrutmydocs.repositories.fs.FSSMDRepositoryData;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class TestRepositories extends ScrutMyDocsTests {

	@Test
	public void testIndexRepository() throws Exception {
		FSSMDRepositoryData repository = new FSSMDRepositoryData();
        repository.id = "test-fs";
				
		SMDRepositoriesFactory.getInstance().save(repository);

        List<SMDRepositoryData> repositories = SMDRepositoriesFactory.getInstance().getRepositories();

        assertThat(repositories.size(), is(1));

        SMDRepositoryData plugin = repositories.get(0);
        assertThat(plugin.type, is(repository.type));
        // TODO Fix that. We send a JSON which has id. We get it back but it's not deserialized
        assertThat(plugin.id, notNullValue());
        assertThat(plugin.id, is(repository.id));
        assertThat(plugin.type, is(repository.type));
        assertThat(plugin.url, is(repository.url));
	}
}
