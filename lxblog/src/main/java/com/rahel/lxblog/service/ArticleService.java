package com.rahel.lxblog.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rahel.lxblog.dao.ArticleDao;
import com.rahel.lxblog.dao.Tag_ArticleDao;
import com.rahel.lxblog.dao.UserDao;
import com.rahel.lxblog.dto.ArticleRequest;
import com.rahel.lxblog.dto.ArticleResponse;
import com.rahel.lxblog.entity.Article;
import com.rahel.lxblog.entity.BlogUser;
import com.rahel.lxblog.entity.Status;
import com.rahel.lxblog.entity.Tag_Article;

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

	public List<ArticleResponse> getAllPublicArticles() {
		ArrayList<Article> articles = (ArrayList<Article>) articleDao.findAllByStatus(Status.PUBLIC.statusName);
		return getResponsesList(articles);
	}

	public List<ArticleResponse> getAllCurrentUserArticles(Integer user_id) {
		ArrayList<Article> articles = (ArrayList<Article>) articleDao.findAllByAuthor_id(user_id);
		return getResponsesList(articles);
	}

	public List<ArticleResponse> getArticlesByTag(List<String> tags) {
		List<Tag_Article> taPairs = tagService.getTApairsByTag(tags);
		Set<Integer> articleIds = new HashSet<>();
		taPairs.forEach(tap->articleIds.add(tap.getArticle_id()));
		ArrayList<Article> articles = new ArrayList<>();
		for(Integer i: articleIds){
		Optional<Article> article =articleDao.findById(i);
		if(article.isPresent()) {
			articles.add(article.get());
		}
		}
	    return getResponsesList(articles);
	}
	
	public List<ArticleResponse> getResponsesList(List<Article> articles) {
		ArrayList<ArticleResponse> articleResponses = new ArrayList<>();
		for (int i = 0; i < articles.size(); i++) {
			List<String> tags = tagService.getAllTagsForThisArticle(articles.get(i).getId());
			ArticleResponse articleResponse = new ArticleResponse();
			articleResponse.getFromArticle(articles.get(i));
			articleResponse.setTags(tags);
			BlogUser blogUser = userDao.findById(articles.get(i).getAuthor_id()).orElse(null);
			if (blogUser != null) {
				articleResponse.setAuthor_first_name(blogUser.getFirst_name());
				articleResponse.setAuthor_last_name(blogUser.getLast_name());
			}
			articleResponses.add(articleResponse);
		}
		return articleResponses;
	}

	public void saveArticle(ArticleRequest articleRequest, Integer user_id) {
		Article article = new Article();
		article.updateArticle(articleRequest);
		article.setCreated_at(new java.sql.Date(new java.util.Date().getTime())); // adding when created
		article.setAuthor_id(user_id);
		article = articleDao.save(article);

		ArrayList<String> tags = (ArrayList<String>) articleRequest.getTags();
		tagService.updateTags(tags, article.getId());

	}

	public void editArticle(ArticleRequest articleRequest, Integer article_id, Integer user_id) {
		Article article = articleDao.findById(article_id).orElse(null);
		if (article == null) {
			return;
		}
		if (article.getAuthor_id() != user_id) {
			System.out.println("Only author is allowed to edit the article");
			return;
		}
		article.updateArticle(articleRequest);
		article.setUpdated_at(new java.sql.Date(new java.util.Date().getTime()));
		articleDao.save(article);
		// now updating the tags
		ArrayList<String> tags = (ArrayList<String>) articleRequest.getTags();
		tagService.updateTags(tags, article_id);

	}

	public void deleteArticle(Integer article_id, Integer user_id) {
		Article article = articleDao.findById(article_id).orElse(null);
		if (article == null) {
			return;
		}
		Integer autor_id = article.getAuthor_id();
		if (user_id == autor_id) {
			articleDao.deleteById(article_id);

			taDao.deleteAllByArticle(article_id); // now deleting tags_articles pairs
		} else
			System.out.println("Any article is to be deleted by its owner only");
	}

	public List<ArticleResponse> getRequiredPublicArticles(Integer skip, Integer limit, Integer author, String sort,
			String order) {
		Pageable pageable;
		if(order.equals("asc")){
		pageable = PageRequest.of(skip, limit, Sort.by(sort).ascending());
		}
		else {
		pageable = PageRequest.of(skip, limit, Sort.by(sort).descending());	
		}
		ArrayList<Article> articles = (ArrayList<Article>) articleDao.findAllByAuthor_id(author, Status.PUBLIC.name(),pageable);
		return getResponsesList(articles);
	}

}
