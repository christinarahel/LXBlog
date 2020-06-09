package com.rahel.lxblog.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.rahel.lxblog.model.ArticleRequest;

@Entity
@Table(name="article")
public class Article implements Serializable { // to add tags after

	private Integer id;

	private String title;

	private String text; 

	private String status;

	private int author_id;

	private Date created_at; 

	private Date updated_at; 
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(java.sql.Date date) {
		this.created_at = date;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(java.sql.Date updated_at) {
		this.updated_at = updated_at;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", text=" + text + ", status=" + status + ", author_id="
				+ author_id + ", created_at=" + created_at + ", updated_at=" + updated_at + "]";
	}

	public void updateArticle(ArticleRequest articleRequest) {
		this.title=articleRequest.getTitle();
		this.text =articleRequest.getText();
		this.status=articleRequest.getStatus();
	}
}
