package org.scrutmydocs.security;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

public abstract class SMDSecurity {

	public abstract List<String> schekUserGroups(MultivaluedMap<String, String> httpHeaders);

	public List<String> schekGroup(MultivaluedMap<String, String> httpHeaders) {

		List<String> groups = new ArrayList<String>();

		List<String> listGp = schekUserGroups(httpHeaders);

		if (listGp != null) {

			for (String gp : listGp) {
				groups.add(gp);
			}

		}

		groups.add("ANONYME");

		return groups;
	}

}
