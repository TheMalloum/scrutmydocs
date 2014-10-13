package org.scrutmydocs.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.scrutmydocs.annotations.SMDRegisterRepositoryData;
import org.scrutmydocs.security.Group;

import com.sun.istack.NotNull;

public class SMDRepositoryData {

	private static HashMap<String, SMDRepositoryScan> listScan;

	private static HashMap<String, Class<? extends SMDRepositoryData>> listData;

	public static SMDRepositoryScan getScanInstance(String type) {
		return listScan.get(type);
	}

	public static Class<? extends SMDRepositoryData> getTypeRepository(
			String type) {
		return listData.get(type);
	}

	public SMDRepositoryData() {
		SMDRegisterRepositoryData myRegister = this.getClass().getAnnotation(
				SMDRegisterRepositoryData.class);
		if (myRegister != null) {
			this.type = myRegister.name();
		}
		groups.add(Group.ANONYME.name());
		
		
	}

	public String id;


	@NotNull
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
