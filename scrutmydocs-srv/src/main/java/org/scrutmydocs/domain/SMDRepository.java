package org.scrutmydocs.domain;

import org.scrutmydocs.annotations.SMDRegisterRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SMDRepository {

	private static HashMap<String, SMDRepositoryScan> listScan;

	private static HashMap<String, Class<? extends SMDRepository>> listData;

	public static SMDRepositoryScan getScanInstance(String type) {
		return listScan.get(type);
	}

	public static Class<? extends SMDRepository> getTypeRepository(
			String type) {
		return listData.get(type);
	}

	public SMDRepository() {
		SMDRegisterRepository myRegister = this.getClass().getAnnotation(
				SMDRegisterRepository.class);
		if (myRegister != null) {
			this.type = myRegister.name();
		}
		groups.add("ANONYMOUS");
	}

	public String id;

	public String url;
	
	public String type;

	public Date lastScan;

	public boolean start;
	
	public List<String> groups =new ArrayList<String>();

	public void start() {
		start = true;
	}

	public void stop() {
		start = false;
	}

}
