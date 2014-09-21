package org.scrutmydocs.scan;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scrutmydocs.plugins.SMDAbstractPlugin;
import org.scrutmydocs.search.SMDSettingsFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScanDocuments {
	private Logger logger = LogManager.getLogger(ScanDocuments.class);

    @Scheduled(cron="*/30 * * * * ?")
	public void scan() {
        logger.info("start scan()");

        List<SMDAbstractPlugin> settings = SMDSettingsFactory.getInstance().getSettings();
        if (settings == null) {
            logger.info("No settings. Skipping...");
            return;
        }

        for (SMDAbstractPlugin plugin : settings) {
				logger.info("checking plugin " + plugin);
				plugin.scrut();
        }

        logger.info("end scan()");
	}
}
