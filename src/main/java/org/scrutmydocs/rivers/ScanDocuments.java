package org.scrutmydocs.rivers;

import org.apache.log4j.Logger;
import org.scrutmydocs.contract.SMDSettings;
import org.scrutmydocs.plugins.SMDAbstractPlugin;
import org.scrutmydocs.settings.SMDSettingsFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScanDocuments {
	private Logger logger = Logger.getLogger(ScanDocuments.class);

    @Scheduled(cron="*/30 * * * * ?")
	public void scan() {
        logger.info("start scan()");
		Date lastScan = new Date();

        SMDSettings settings = SMDSettingsFactory.getInstance().getSettings();
        if (settings == null) {
            logger.info("No settings. Skipping...");
            return;
        }

        for (SMDAbstractPlugin plugin : settings.smdAbstractPlugins) {
				logger.info("checking plugin " + plugin);
				plugin.changes(settings.lastScan);
        }

        settings.lastScan = lastScan;
        SMDSettingsFactory.getInstance().saveSettings(settings);
        logger.info("end scan()");
	}
}
