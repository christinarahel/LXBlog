package com.rahel.lxblog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rahel.lxblog.model.ArticleModel;
import com.rahel.lxblog.service.ArticleService;


@RestController
public class RestArticleController {

	@Autowired
	private ArticleService articleService;
	
	@GetMapping("/articles")
	public List<ArticleModel> getAllPublicArticles() {
		
		System.out.println("RestArticleControlle      all public articles");
		
		return articleService.getAllPublicArticles();
	}
	
	/*@PostMapping("/articles") 
	public void createArticle(@RequestBody Article article) {
		System.out.println("RestArticleControlle   adding new article");
		articleDaoImpl.createArticle(article);
	}
	
	@PutMapping("/articles/{id}")
	public void editArticle(@RequestBody Article article, @PathVariable("id") Integer id) {
		System.out.println("RestArticleControlle   editing existing article");
		articleDaoImpl.editArticle(article,id);
	}
	
	@DeleteMapping("/articles/{id}")
	public @ResponseBody void deleteArticle(@PathVariable("id") Integer id) {
		System.out.println("RestArticleControlle   deleting existing article");
		articleDaoImpl.deleteArticle(id);
	}
	
	@GetMapping("/my")
	public List<Article> getAllArticlesOfThisAuther() {
		System.out.println("RestArticleControlle   getting all my articles");
		Integer author_id = 10;
		return articleDaoImpl.getAllArticlesOfThisAuther(author_id);
	}*/
	
}
