package org.scrutmydocs.repositories;

import org.junit.Assert;
import org.junit.Test;
import org.scrutmydocs.contract.SMDRepository;
import org.scrutmydocs.repositories.SMDRepositoriesFactory;
import org.scrutmydocs.repositories.fs.FSSMDPlugin;

public class TestRepositories {

	
	@Test
	public void testIndexRepository() throws Exception {
		
		FSSMDPlugin repository = new FSSMDPlugin("url");
				
		SMDRepositoriesFactory.getInstance().save(repository);
		
		Thread.sleep(6*1000);
		
		for(SMDRepository plugin : SMDRepositoriesFactory.getInstance().getRepositories()){
			
			Assert.assertEquals(plugin.url, repository.url);
			
			
			
			repository = (FSSMDPlugin) SMDRepositoriesFactory.getInstance().get(plugin.id);
			 
			Assert.assertEquals(plugin.url, repository.url);
			Assert.assertEquals(plugin.id, repository.id);
			Assert.assertEquals(plugin.type, repository.type);
			 
			return;
			
		}
		
		
		Assert.fail();
		
	}
	
	
	
}
