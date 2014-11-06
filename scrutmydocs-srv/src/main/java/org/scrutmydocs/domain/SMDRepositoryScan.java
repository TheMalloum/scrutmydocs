package org.scrutmydocs.domain;

import org.scrutmydocs.annotations.SMDRegisterRepositoryScan;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public abstract class SMDRepositoryScan {

	public SMDRepositoryScan() {
		SMDRegisterRepositoryScan myRegister = this.getClass().getAnnotation(SMDRegisterRepositoryScan.class);
		this.type = myRegister.name();
	}
	
	public final String type;
	

	public abstract  void  scrut(SMDRepository smdRepository);
	
	public abstract  boolean check(SMDRepository smdRepository);

	public abstract  byte[]  get(SMDDocument document);

	public abstract void  delete(SMDRepository smdRepository);

}
