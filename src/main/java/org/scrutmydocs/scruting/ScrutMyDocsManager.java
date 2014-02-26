package org.scrutmydocs.scruting;

import java.util.List;

import org.scrutmydocs.datasource.DataSourceManager;
import org.scrutmydocs.datasource.data.SMDChanges;
import org.scrutmydocs.datasource.data.SMDDataSource;
import org.scrutmydocs.documents.SMDDocument;
import org.scrutmydocs.search.SearchFactory;

public class ScrutMyDocsManager {

	
	
	public void scruting() {

		for (SMDDataSource smdDataSource : DataSourceManager
				.getListDataSources()) {

			String since = checkSince(smdDataSource);

			List<SMDChanges> changes = smdDataSource.changes(since);

			for (SMDChanges smdChanges : changes) {

				if (smdChanges.delete) {
					SearchFactory.getInstance().delete(smdChanges.idDocument,
							smdDataSource);
				} else {
					SMDDocument smdDocument = smdDataSource
							.getDocument(smdChanges.idDocument);
					
					SearchFactory.getInstance().indexe(smdDocument, smdDataSource);

				}

			}

		}

	}

	private String checkSince(SMDDataSource smdDataSource) {
		// TODO Auto-generated method stub
		return null;
	}
}
