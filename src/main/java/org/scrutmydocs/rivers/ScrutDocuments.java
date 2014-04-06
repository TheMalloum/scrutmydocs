package org.scrutmydocs.rivers;

import java.util.List;

import org.apache.log4j.Logger;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.datasource.SMDDataSource;
import org.scrutmydocs.search.SMDSearchFactory;

public class ScrutDocuments {

	private Logger logger = Logger.getLogger(ScrutDocuments.class.getName());

	public ScanDataSource datasoures = new ScanDataSource();

	public void scruting() {
		// checkout all conf datasources register

		for (SMDDataSource smdDataSource : ScanDataSource.getAll().values()) {

			List<SMDDataSource> dataSourcesSave = SMDSearchFactory
					.getInstance().getConf(smdDataSource);

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
					SMDSearchFactory.getInstance().index(dataSourceSave,
							smdDocument);
					if (smdDocument.date.after(dataSourceSave.date)) {
						dataSourceSave.date = smdDocument.date;
					}

				}
				SMDSearchFactory.getInstance().saveConf(smdDataSource);
			}

		}

	}
}
