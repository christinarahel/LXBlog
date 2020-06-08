package com.rahel.lxblog.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rahel.lxblog.dao.ArticleDao;
import com.rahel.lxblog.dao.Tag_ArticleDao;
import com.rahel.lxblog.dao.UserDao;
import com.rahel.lxblog.entity.Article;
import com.rahel.lxblog.entity.BlogUser;
import com.rahel.lxblog.entity.Status;
import com.rahel.lxblog.entity.Tag_Article;
import com.rahel.lxblog.model.ArticleModel;

@Service
@Transactional
public class ArticleService {
	
	@Autowired
	private ArticleDao articleDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private Tag_ArticleDao taDao;
	
	public List<ArticleModel> getAllPublicArticles(){
		ArrayList<Article> articles = new ArrayList<>();
		ArrayList<ArticleModel> articleModels = new ArrayList<>();
		articles = articleDao.findAllByStatus(Status.PUBLIC.status);
		for(int i=0; i<articles.size(); i++) {
	    	List<String> tags =tagService.getAllTagsForThisArticle(articles.get(i).getId());    
	//		List<Tag_Article> ta =taDao.findAllByArticle_id(articles.get(i).getId()); //.getAllTagsForThisArticle(articles.get(i).getId());
			ArticleModel articleModel =new ArticleModel(articles.get(i), tags);
			BlogUser blogUser = userDao.findById(articles.get(i).getAuthor_id()).orElse(null); 
			if(blogUser!=null) {
			articleModel.setAuthor_first_name(blogUser.getFirst_name());
			articleModel.setAuthor_last_name(blogUser.getLast_name());
			
	//		articleModel.setTags(tags);
			}
			articleModels.add(articleModel);
		}
		return articleModels;
	}
	
	public List<ArticleModel> getAllCurrentUserArticles(Integer user_id) {
		ArrayList<Article> articles = new ArrayList<>();
		ArrayList<ArticleModel> articleModels = new ArrayList<>();
		articles = articleDao.findAllByAuthor_id(user_id);
		
		for(int i=0; i<articles.size(); i++) {
	    	List<String> tags =tagService.getAllTagsForThisArticle(articles.get(i).getId());    
			ArticleModel articleModel =new ArticleModel(articles.get(i), tags);
			BlogUser blogUser = userDao.findById(user_id).orElse(null); 
			if(blogUser!=null) {
			articleModel.setAuthor_first_name(blogUser.getFirst_name());
			articleModel.setAuthor_last_name(blogUser.getLast_name());

			}
			articleModels.add(articleModel);
		}
		return articleModels;
	}

	public void saveArticleModel(ArticleModel articleModel, Integer user_id) {
		Article article=getArticleFromModel(articleModel);
		article.setCreated_at(new java.sql.Date(new java.util.Date().getTime()));  //adding when created
		article.setAuthor_id(user_id);
		article =articleDao.save(article);  
		
		System.out.println(article + "public void saveArticleModel(ArticleModel articleModel, String userEmail)");
		
		ArrayList<String> tags =(ArrayList<String>)articleModel.getTags();	
		tagService.updateTags(tags, article.getId());
		
	}
	
	public void editArticle(ArticleModel articleModel, Integer article_id, Integer user_id) {
		Article oldArticle = articleDao.findById(article_id).orElse(null);
		if(oldArticle==null) {return;}
		if(oldArticle.getAuthor_id()!=user_id) {
			System.out.println("Only author is allowed to edit the article"); 
			return;
			}
		Article updatedArticle=getArticleFromModel(articleModel);
		updatedArticle.setCreated_at(new java.sql.Date(articleModel.getCreated_at().getTime())); 
		updatedArticle.setUpdated_at(new java.sql.Date(new java.util.Date().getTime()));
		updatedArticle.setId(article_id);
		updatedArticle.setAuthor_id(oldArticle.getAuthor_id());
		updatedArticle =articleDao.save(updatedArticle);
		
		// now updating the tags
		ArrayList<String> tags =(ArrayList<String>)articleModel.getTags();	
		tagService.updateTags(tags, article_id);
		
	}
	
	public Article getArticleFromModel(ArticleModel articleModel) {
		Article article =new Article();
		article.setTitle(articleModel.getTitle());
		article.setText(articleModel.getTitle());
		article.setStatus(articleModel.getStatus());
		return article;
	}

	public void deleteArticle(Integer article_id, Integer user_id) {
		Article article = articleDao.findById(article_id).orElse(null);
		if(article==null) {return;}
		Integer autor_id=article.getAuthor_id();
		if(user_id==autor_id) {  
		articleDao.deleteById(article_id);	
		// now deleting tags_articles pairs
		taDao.deleteAllByArticle(article_id);
		}
		else System.out.println("Any article is to be deleted by its owner only");
	}
	
}
