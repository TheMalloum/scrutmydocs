package org.scrutmydocs.contract;

import java.util.Date;
import java.util.List;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class SMDDataSource {

	protected ESLogger logger = Loggers.getLogger(getClass().getName());

	public SMDDataSource(ObjectNode json) {

		logger.debug(json.toString());
	}

	public abstract String getID();

	public abstract List<SMDChanges> changes(Date since);

	public abstract String getDocumentPath(String id);

	public abstract SMDDocument getDocument(String id);

	public abstract Date checkSince();

	public abstract String updateSince(String since);

	public abstract List<SMDDataSource> listDS();

}
