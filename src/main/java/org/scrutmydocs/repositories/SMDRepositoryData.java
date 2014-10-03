package org.scrutmydocs.repositories;

import java.util.Date;

import org.scrutmydocs.repositories.annotations.SMDRegisterRepositoryData;

public class SMDRepositoryData {

	public SMDRepositoryData() {
		SMDRegisterRepositoryData myRegister = this.getClass().getAnnotation(
				SMDRegisterRepositoryData.class);
		if (myRegister != null) {
			this.type = myRegister.name();
		}
	}

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
