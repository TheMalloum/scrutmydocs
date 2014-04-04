package org.scrutmydocs.contract;

import java.util.Date;

public class SMDChanges {

	public ChangeType changeType;

	public String idDocument;

	public Date changeDate;

	public SMDChanges(ChangeType changeType, String idDocument, Date date) {
		super();
		if (date == null || idDocument == null || changeType == null) {
			throw new IllegalArgumentException(
					"date, idDocument or changeType can't be null");
		}

		this.changeType = changeType;
		this.idDocument = idDocument;
		this.changeDate = date;
	}

	public enum ChangeType {

		DELETE, UPDATE, CREATE;

	}

}
