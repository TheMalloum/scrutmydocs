package org.scrutmydocs.settings;

import org.scrutmydocs.contract.SMDSettingsService;

public class SMDSettingsFactory {

	private static SMDSettingsService settingsService;

    /**
     * We instantiate an elasticsearch backend implementation
     */
	public static synchronized SMDSettingsService getInstance() {
		if (settingsService == null) {
			settingsService = new SettingsServiceImpl();
		}

		return settingsService;
	}
	
}
