package org.scrutmydocs.security;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.scrutmydocs.annotations.SMDRegisterSecurityPolicy;

@SMDRegisterSecurityPolicy
public class SMDDefaultSecurityPolicy extends SMDSecurity {

	@Override
	public List<String> schekUserGroups(MultivaluedMap<String, String> httpHeaders) {
	
		return null;
	}

}
