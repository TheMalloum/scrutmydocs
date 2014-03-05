package org.scrutmydocs.scruting;

import java.util.List;

import org.scrutmydocs.contract.SMDChanges;
import org.scrutmydocs.contract.SMDChanges.ChangeType;
import org.scrutmydocs.contract.SMDDataSource;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.datasource.SMDDataSourcesManager;
import org.scrutmydocs.search.SMDSearchFactory;

public class ScrutMyDocsManager {

	public void scruting() {

		for (SMDDataSource smdDataSource : SMDDataSourcesManager
				.getListDataSources()) {

			String since = smdDataSource.checkSince();

			List<SMDChanges> changes = smdDataSource.changes(since);

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
