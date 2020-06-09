package com.rahel.lxblog.model;

import java.util.List;

public class ArticleRequest {

	private String title;

	private String text; 

	private String status;
	
	private List<String> tags;
	
	public ArticleRequest() {
		
	}
	
	/*public ArticleModel(Article article, List<String> tags) {
		this.id = article.getId();
		this.title=article.getTitle();
		this.text=article.getText();
		this.status=article.getStatus();
		this.created_at= new java.util.Date(article.getCreated_at().getTime());// toLocalDate()); to edit this shit
		if(article.getUpdated_at()!=null) {
			this.updated_at= new java.util.Date(article.getUpdated_at().getTime());
		}
		this.tags=tags;
	}*/

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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
