package org.scrutmydocs.security;

public enum Group {
	ROOT("root"), ANONYME("");

	private String name = "";

	// Constructeur
	Group(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
