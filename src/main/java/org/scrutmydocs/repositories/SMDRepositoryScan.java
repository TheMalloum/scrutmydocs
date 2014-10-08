package org.scrutmydocs.repositories;

import org.scrutmydocs.repositories.annotations.SMDRegisterRepositoryScan;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public abstract class SMDRepositoryScan {

	
	
	public SMDRepositoryScan() {
		SMDRegisterRepositoryScan myRegister = this.getClass().getAnnotation(SMDRegisterRepositoryScan.class);
		this.type = myRegister.name();

	}
	
	public final String type;
	
	

	public abstract  void scrut(SMDRepositoryData smdRepositoryData);
	
	public abstract  boolean check(SMDRepositoryData smdRepositoryData);

	public abstract  byte[]  get(SMDRepositoryData smdRepositoryData);

}
