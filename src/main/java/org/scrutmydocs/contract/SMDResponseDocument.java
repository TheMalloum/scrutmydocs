package org.scrutmydocs.contract;

import java.util.Collection;

public class SMDResponseDocument {

	public final SMDDocument document;
	public final Collection<String> highlights;

    public SMDResponseDocument(SMDDocument document, Collection<String> highlights) {
        this.document = document;
        this.highlights = highlights;
    }
}
