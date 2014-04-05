package org.scrutmydocs.contract;

import java.util.Date;

public class SMDChanges {

	public ChangeType changeType;

	public SMDDocument document;

	public Date changeDate;

	public SMDChanges(ChangeType changeType, SMDDocument document, Date date) {
		super();
		if (date == null || document == null || changeType == null) {
			throw new IllegalArgumentException(
					"date, idDocument or changeType can't be null");
		}

		this.changeType = changeType;
		this.document = document;
		this.changeDate = date;
	}

	public enum ChangeType {

		DELETE, UPDATE, CREATE;

	}

}
