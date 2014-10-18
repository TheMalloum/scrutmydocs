package org.scrutmydocs.security;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

public interface SMDSecurity {

	public List<String> schekGroups(MultivaluedMap<String, String> httpHeaders);
}
