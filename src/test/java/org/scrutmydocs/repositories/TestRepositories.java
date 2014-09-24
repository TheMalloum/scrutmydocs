package org.scrutmydocs.repositories;

import org.junit.Assert;
import org.junit.Test;
import org.scrutmydocs.repositories.SMDAbstractRepository;
import org.scrutmydocs.repositories.SMDRepositoriesFactory;
import org.scrutmydocs.repositories.fs.FSSMDPlugin;

public class TestRepositories {

	
	@Test
	public void testIndexSetting() throws Exception {
		
		FSSMDPlugin repository = new FSSMDPlugin("url");
				
		SMDRepositoriesFactory.getInstance().save(repository);
		
		Thread.sleep(6*1000);
		
		for(SMDAbstractRepository plugin : SMDRepositoriesFactory.getInstance().getRepositories()){
			
			Assert.assertEquals(plugin.url, repository.url);
			
			
			
			repository = (FSSMDPlugin) SMDRepositoriesFactory.getInstance().get(plugin.id);
			 
			Assert.assertEquals(plugin.url, repository.url);
			Assert.assertEquals(plugin.id, repository.id);
			Assert.assertEquals(plugin.name, repository.name);
			 
			return;
			
		}
		
		
		Assert.fail();
		
	}
	
	
	
}
