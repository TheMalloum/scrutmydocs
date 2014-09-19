package org.scrutmydocs.plugins;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.scrutmydocs.contract.SMDDocument;

import org.scrutmydocs.search.SMDSearchFactory;

//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.WRAPPER_OBJECT, property="type")
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="class")

public abstract class SMDAbstractPlugin {

	
	public String name() {
		SMDPlugin myRegister = this.getClass().getAnnotation(
				SMDPlugin.class);
		return myRegister.name();

	}

    public Long updateRate;

    public String includes;

    public String excludes;

    public String analyzer;

    public String url;

	public boolean start;

	public abstract void scrut(Date since);


}
