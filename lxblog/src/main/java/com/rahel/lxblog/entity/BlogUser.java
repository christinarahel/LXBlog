package com.rahel.lxblog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.rahel.lxblog.controller.RegistrationRequest;

@Entity
@Table(name="bloguser")
public class BlogUser {

	private	Integer id;

	private	String first_name;

	private String last_name;

	private String password;

	private String email;

	private java.sql.Date created_at;
	
	private String role_name="USER";
	
	private int is_active = 0;
	
	public int getIs_active() {
		return is_active;
	}

	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}

	public BlogUser() {	
	}
	
     public BlogUser(RegistrationRequest registrationRequest) {
		this.first_name=registrationRequest.getFirst_name();
		this.last_name =registrationRequest.getLast_name();
	    this.password =registrationRequest.getPassword();
	    this.is_active=0;
		this.email=registrationRequest.getEmail();
		this.created_at =new java.sql.Date(new java.util.Date().getTime());// is it fine to do it in the constuctor????? 				
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")   // not necessary if the name is matching to the column name
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public java.sql.Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(java.sql.Date created_at) {
		this.created_at = created_at;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	
	@Override
	public String toString() {
		return "BlogUser [id=" + id + ", first_name=" + first_name + ", last_name=" + last_name + ", password="
				+ password + ", email=" + email + ", created_at=" + created_at + ", role_name=" + role_name + "]";
	}

}
