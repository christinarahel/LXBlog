package com.rahel.lxblog.entity;

public enum Status {
	  PUBLIC("public"), DRAFT("draft");
		public String status;

	Status(String status) {
		this.status=status;
	}
	String status() {
		return status;
	}
	}

