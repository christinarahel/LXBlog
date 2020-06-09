package com.rahel.lxblog.model;

import java.util.Date;
import java.util.List;

import com.rahel.lxblog.entity.Article;

public class ArticleResponse {
	
	private Integer id;

	private String title;

	private String text; 

	private String status;

	private String author_first_name;
	
	private String author_last_name;

	private Date created_at; 

	private Date updated_at=null;
	
	private List<String> tags;
	
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

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public void getFromArticle(Article article) {
		this.id = article.getId();
		this.title=article.getTitle();
		this.text=article.getText();
		this.status=article.getStatus();
		this.created_at= new java.util.Date(article.getCreated_at().getTime());// toLocalDate()); to edit this shit
		if(article.getUpdated_at()!=null) {
			this.updated_at= new java.util.Date(article.getUpdated_at().getTime());
		}
	}
}
