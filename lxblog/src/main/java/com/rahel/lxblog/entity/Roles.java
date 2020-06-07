package com.rahel.lxblog.entity;

public enum Roles {
	ADMIN("ADMIN"), USER("USER"), ANONIMOUS("ANONIMOUS");

	private String role;

	Roles(String role) {
		this.role = role;
	}

	public String role() {
		return role;
	}
}
