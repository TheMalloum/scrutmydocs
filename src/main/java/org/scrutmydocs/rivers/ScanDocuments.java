package org.scrutmydocs.rivers;

import java.util.Date;

import org.apache.log4j.Logger;
import org.scrutmydocs.contract.SMDSettings;
import org.scrutmydocs.plugins.SMDAbstractPlugin;
import org.scrutmydocs.settings.SMDSettingsFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class ScanDocuments {
	private Logger logger = Logger.getLogger(ScanDocuments.class);

    @Scheduled(cron="*/5 * * * * ?")
	public void scan() {
		Date lastScan = new Date();

        SMDSettings settings = SMDSettingsFactory.getInstance().getSettings();

        for (SMDAbstractPlugin plugin : settings.smdAbstractPlugins) {
				logger.info("checking plugin " + plugin);
				plugin.changes(settings.lastScan);
        }

        settings.lastScan = lastScan;
        SMDSettingsFactory.getInstance().saveSettings(settings);
	}
}
