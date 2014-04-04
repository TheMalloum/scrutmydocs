package org.scrutmydocs.scruting;

import java.util.Date;
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

public class ScrutDocuments {

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	@Autowired
	protected ApplicationContext context;

	@Autowired
	public ScanDataSource datasoures;

	private void scruting() {
		// checkout all conf datasources register

		for (SMDDataSource smdDataSource : datasoures.list.values()) {

			SMDDataSource conf = SMDSearchFactory.getInstance(smdDataSource)
					.getConf();

			Date date = conf.since();

			List<SMDChanges> changes = smdDataSource.changes(date);

			for (SMDChanges smdChanges : changes) {
				if (smdChanges.changeType == ChangeType.DELETE) {
					SMDSearchFactory.getInstance(smdDataSource).delete(
							smdChanges.idDocument);
				} else {
					SMDDocument smdDocument = smdDataSource
							.getDocument(smdChanges.idDocument);
					SMDSearchFactory.getInstance(smdDataSource).index(
							smdDocument);

				}
			}
		}

	}
}
