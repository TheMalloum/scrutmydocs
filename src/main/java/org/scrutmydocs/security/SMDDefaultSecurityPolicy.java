package org.scrutmydocs.security;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.scrutmydocs.annotations.SMDRegisterSecurityPolicy;

@SMDRegisterSecurityPolicy
public class SMDDefaultSecurityPolicy implements SMDSecurity {

	@Override
	public List<String> schekGroups(MultivaluedMap<String, String> httpHeaders) {
		List<String> groups = new ArrayList<String>();
		groups.add("ANONYME");
		return groups;
	}

}
