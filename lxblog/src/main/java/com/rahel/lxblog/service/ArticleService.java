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
	
	
	
//	@Autowired  to add tags lists here
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
}
