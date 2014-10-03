package org.scrutmydocs.repositories;

import java.util.Date;

public  class SMDRepositoryData {

//	public String determineType() {
//		SMDRegisterRepositoryData myRegister = this.getClass().getAnnotation(
//				SMDRegisterRepositoryData.class);
//		return myRegister.name();
//	};

	public String id;
	
	public String type;

	public Date lastScan;

	public boolean start;

	public void start() {
		start = true;
	}

	public void stop() {
		start = false;
	}

}
