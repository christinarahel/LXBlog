package com.rahel.lxblog.dto;

public class CommentResponse {

	private int id;

	private String message;
	
    private String author_first_name=null;
	
	private String author_last_name=null;

	private java.util.Date created_at;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthor_first_name() {
		return author_first_name;
	}

	public void setAuthor_first_name(String author_first_name) {
		this.author_first_name = author_first_name;
	}

	public String getAuthor_last_name() {
		return author_last_name;
	}

	public void setAuthor_last_name(String author_last_name) {
		this.author_last_name = author_last_name;
	}

	public java.util.Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(java.util.Date created_at) {
		this.created_at = created_at;
	}

}
