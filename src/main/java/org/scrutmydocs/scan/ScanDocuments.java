package org.scrutmydocs.scan;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scrutmydocs.contract.SMDSettings;
import org.scrutmydocs.plugins.SMDAbstractPlugin;
import org.scrutmydocs.settings.SMDSettingsFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScanDocuments {
	private Logger logger = LogManager.getLogger(ScanDocuments.class);

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
				plugin.scrut(settings.lastScan);
        }

        settings.lastScan = lastScan;
        SMDSettingsFactory.getInstance().saveSettings(settings);
        logger.info("end scan()");
	}
}
