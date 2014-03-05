package org.scrutmydocs.contract;

public class SMDChanges {

	public ChangeType changeType;

	public String idDocument;

	public SMDChanges(ChangeType changeType, String idDocument) {
		super();
		this.changeType = changeType;
		this.idDocument = idDocument;
	}

	public enum ChangeType {

		DELETE, UPDATE, CREATE;

	}

}
