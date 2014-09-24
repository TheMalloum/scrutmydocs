package org.scrutmydocs.contract;

import java.util.Date;

import org.scrutmydocs.repositories.SMDRepositoryRegister;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public abstract class SMDRepository {

	
	
	public SMDRepository() {
		SMDRepositoryRegister myRegister = this.getClass().getAnnotation(SMDRepositoryRegister.class);
		this.type = myRegister.name();

	}
	
	public String id;

	public final String type;

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
