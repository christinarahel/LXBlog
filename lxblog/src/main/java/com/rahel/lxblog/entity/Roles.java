package com.rahel.lxblog.entity;

public enum Roles {
	ADMIN("ROLE_ADMIN"), USER("ROLE_USER"), ANONIMOUS("ROLE_ANONIMOUS");

	private String name;

	Roles(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
