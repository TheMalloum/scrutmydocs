package org.scrutmydocs.rivers;

import java.util.List;

import org.apache.log4j.Logger;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.datasource.SMDDataSource;
import org.scrutmydocs.search.SMDSearchFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ScrutDocuments {

	private static Logger logger = Logger.getLogger(ScrutDocuments.class.getName());

	@Autowired
	protected static ApplicationContext context;

	@Autowired
	public static ScanDataSource datasoures;

	public static void scruting() {
		// checkout all conf datasources register

		for (SMDDataSource smdDataSource : datasoures.list.values()) {

			List<SMDDataSource> dataSourcesSave = SMDSearchFactory.getInstance(
					smdDataSource).getConf();

			for (SMDDataSource dataSourceSave : dataSourcesSave) {
				logger.info("extracting modified files form the directory "
						+ dataSourceSave.url);

				List<SMDDocument> changes = dataSourceSave	
						.changes(dataSourceSave.date);

				logger.info(changes.size()
						+ " modified files extract from the directory "
						+ dataSourceSave.url);

				if (changes == null || changes.isEmpty()) {
					continue;
				}
				for (SMDDocument smdDocument : changes) {

					logger.trace("extract document " + smdDocument.id);
					SMDSearchFactory.getInstance(dataSourceSave).index(
							smdDocument);
					if (smdDocument.date.after(dataSourceSave.date)) {
						dataSourceSave.date = smdDocument.date;
					}

				}
				SMDSearchFactory.getInstance(dataSourceSave).saveConf();
			}

		}

	}
}
