package org.scrutmydocs.plugins;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public abstract class SMDAbstractPlugin {

	
	
	public SMDAbstractPlugin() {
		SMDPlugin myRegister = this.getClass().getAnnotation(SMDPlugin.class);
		this.name = myRegister.name();

	}
	
	public String id;

	public final String name;

	public void start() {
		start=true;
	}

	public void stop() {
		start=false;
	}

	public Date lastScan;

	public Long updateRate;

	public String includes;

	public String excludes;

	public String analyzer;

	public String url;

	public boolean start;

	public abstract void scrut();

}
