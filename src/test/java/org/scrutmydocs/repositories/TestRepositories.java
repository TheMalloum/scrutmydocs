package org.scrutmydocs.repositories;

import org.junit.Assert;
import org.junit.Test;
import org.scrutmydocs.repositories.fs.FSSMDRepositoryData;

public class TestRepositories {

	
	@Test
	public void testIndexRepository() throws Exception {
		
		FSSMDRepositoryData repository = new FSSMDRepositoryData();
				
		SMDRepositoriesFactory.getInstance().save(repository);
		
		Thread.sleep(6*1000);
		
		for(SMDRepositoryData plugin : SMDRepositoriesFactory.getInstance().getRepositories()){
			
			Assert.assertEquals(plugin.type, repository.type);
			
			
			
			repository = (FSSMDRepositoryData) SMDRepositoriesFactory.getInstance().get(plugin.id);
			 
//			Assert.assertEquals(plugin.url, repository.url);
			Assert.assertEquals(plugin.id, repository.id);
			Assert.assertEquals(plugin.type, repository.type);
			 
			return;
			
		}
		
		
		Assert.fail();
		
	}
//	
//	
//	
}
