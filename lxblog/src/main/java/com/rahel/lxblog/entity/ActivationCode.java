package com.rahel.lxblog.entity;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

//@Entity
@RedisHash("usercode")
public class ActivationCode {

	@Id
	private String id;
	
	private	Integer user;
	
	private Date expirationDate;

	public Integer getUser() {
		return user;
	}

	public void setUser(Integer user) {
		this.user = user;
	}

    public String getId() {
		return id;
	}

	public void setId(String code) {
		this.id = code;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Override
	public String toString() {
		return "ActivationCode [id=" + id + ", user=" + user + ", expirationDate=" + expirationDate + "]";
	}
	
}
