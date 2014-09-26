package org.scrutmydocs.repositories;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public abstract class SMDRepositoryData {

	
	
	public SMDRepositoryData() {
		SMDRegisterRepositoryData myRegister = this.getClass().getAnnotation(SMDRegisterRepositoryData.class);
		this.type = myRegister.name();

	}
	public final String type;

	

	public String id;
	
	public String url;
	
	public Date lastScan;
	
	public String includes;

	public String excludes;

	public String analyzer;


	public boolean start;
	
	public void start() {
		start=true;
	}

	public void stop() {
		start=false;
	}



}
