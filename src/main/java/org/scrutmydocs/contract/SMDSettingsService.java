package org.scrutmydocs.contract;

import java.util.List;

import org.scrutmydocs.plugins.SMDAbstractPlugin;

public interface SMDSettingsService {

	public void saveSetting(SMDAbstractPlugin setting);

	public List<SMDAbstractPlugin>  getSettings();

	public SMDAbstractPlugin getSetting(String id);
	
}
