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
	
	
	public Date lastScan;
	
	public boolean start;
	
	public void start() {
		start=true;
	}

	public void stop() {
		start=false;
	}



}
