package org.scrutmydocs.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SMDSearchQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	public String search;
	public int first;
	public int pageSize;
	public List<String> groups;

    public SMDSearchQuery() {
        this.search = null;
        this.first = 0;
        this.pageSize = 10;
        this.groups = new ArrayList<>();
    }

	public SMDSearchQuery(String search, int first, int pageSize, List<String> groups) {
		this.search = search;
		this.first = first;
		this.pageSize = pageSize;
		this.groups = groups;
	}

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SMDSearchQuery{");
        sb.append("search='").append(search).append('\'');
        sb.append(", first=").append(first);
        sb.append(", pageSize=").append(pageSize);
        sb.append(", groups=").append(groups);
        sb.append('}');
        return sb.toString();
    }

    //	public SMDSearchQuery(String search, int first, int pageSize) {
//		this.search = search;
//		this.first = first;
//		this.pageSize = pageSize;
//		 groups.add(Group.ANONYME.name());
//	}

}
