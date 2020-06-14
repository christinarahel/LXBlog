package com.rahel.lxblog.entity;

public enum Status {
	PUBLIC("public"), DRAFT("draft");

	public String statusName;

	Status(String status) {
		this.statusName = status;
	}

	String statusName() {
		return statusName;
	}
}
