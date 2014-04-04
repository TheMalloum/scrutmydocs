package org.scrutmydocs.scruting;

import java.util.List;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.scrutmydocs.contract.SMDChanges;
import org.scrutmydocs.contract.SMDChanges.ChangeType;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.datasource.SMDDataSource;
import org.scrutmydocs.search.SMDSearchFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ScrutDocuments {

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	@Autowired
	protected ApplicationContext context;

	@Autowired
	public ScanDataSource datasoures;

	public void scruting() {
		// checkout all conf datasources register

		for (SMDDataSource smdDataSource : datasoures.list.values()) {

			List<SMDDataSource> dataSourcesSave = SMDSearchFactory.getInstance(
					smdDataSource).getConf();

			for (SMDDataSource dataSourceSave : dataSourcesSave) {

				List<SMDChanges> changes = dataSourceSave
						.changes(dataSourceSave.date);

				if (changes == null || changes.isEmpty()) {
					continue;
				}
				for (SMDChanges smdChanges : changes) {
					if (smdChanges.changeType == ChangeType.DELETE) {
						SMDSearchFactory.getInstance(dataSourceSave).delete(
								smdChanges.idDocument);
					} else {
						SMDDocument smdDocument = dataSourceSave
								.getDocument(smdChanges.idDocument);
						SMDSearchFactory.getInstance(dataSourceSave).index(
								smdDocument);
						dataSourceSave.date = smdChanges.changeDate;
					}
					dataSourceSave.date = smdChanges.changeDate;
				}
				SMDSearchFactory.getInstance(dataSourceSave).saveConf();
			}

		}

	}
}
