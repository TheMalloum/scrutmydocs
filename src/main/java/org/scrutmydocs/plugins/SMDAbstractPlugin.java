package org.scrutmydocs.plugins;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.scrutmydocs.contract.SMDDocument;

import org.scrutmydocs.search.SMDSearchFactory;

//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.WRAPPER_OBJECT, property="type")
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="class")

public abstract class SMDAbstractPlugin {

	public SMDAbstractPlugin(String json) {
	}

	public SMDAbstractPlugin(String id, String url, Long updateRate) {
		this.id = id;
		this.updateRate = updateRate;
		this.url = url;
		this.analyzer = "standard";
	}

	public SMDAbstractPlugin() {
	}

	public String name() {
		SMDPlugin myRegister = this.getClass().getAnnotation(
				SMDPlugin.class);
		return myRegister.name();

	}


    public void index(SMDDocument document) {
        SMDSearchFactory.getInstance().index(this, document);
    }


    public void delete(String id) {
        SMDSearchFactory.getInstance().delete(this, id);
    }

    public String id;

    public Long updateRate;

    public String includes;

    public String excludes;

    public String analyzer;

    public String url;

    public Date date;

	public boolean start;

	public abstract void scrut(Date since);


}
