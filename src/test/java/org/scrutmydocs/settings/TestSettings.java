package org.scrutmydocs.settings;

import org.junit.Assert;
import org.junit.Test;
import org.scrutmydocs.plugins.SMDAbstractPlugin;
import org.scrutmydocs.plugins.fs.FSSMDPlugin;
import org.scrutmydocs.search.SMDSettingsFactory;

public class TestSettings {

	
	@Test
	public void testIndexSetting() throws Exception {
		
		FSSMDPlugin setting = new FSSMDPlugin("url");
				
		SMDSettingsFactory.getInstance().saveSetting(setting);
		
		Thread.sleep(6*1000);
		
		for(SMDAbstractPlugin plugin : SMDSettingsFactory.getInstance().getSettings()){
			
			Assert.assertEquals(plugin.url, setting.url);
			
			
			
			setting = (FSSMDPlugin) SMDSettingsFactory.getInstance().getSetting(plugin.id);
			 
			Assert.assertEquals(plugin.url, setting.url);
			Assert.assertEquals(plugin.id, setting.id);
			Assert.assertEquals(plugin.name, setting.name);
			 
			return;
			
		}
		
		
		Assert.fail();
		
	}
	
	
	
}
