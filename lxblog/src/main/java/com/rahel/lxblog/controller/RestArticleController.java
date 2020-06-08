package com.rahel.lxblog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.rahel.lxblog.config.jwt.JwtProvider;
import com.rahel.lxblog.model.ArticleModel;
import com.rahel.lxblog.service.ArticleService;
import com.rahel.lxblog.service.BlogUserService;


@RestController
public class RestArticleController {

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private BlogUserService userService;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private HttpServletRequest request;
	
	@GetMapping("/articles")
	public List<ArticleModel> getAllPublicArticles() {	
		
		return articleService.getAllPublicArticles();
	}
	
	@PostMapping("/articles") 
	public void createArticle(@RequestBody ArticleModel articleModel) {
	//	System.out.println("RestArticleControlle   adding new article");
		Integer user_id = getCurrentUserID();
    	if(user_id==null) {System.out.println("U are not authorised"); return;}
		articleService.saveArticleModel(articleModel, user_id);
	}
	
	@PutMapping("/articles/{id}")
	public void editArticle(@RequestBody ArticleModel articleModel, @PathVariable("id") Integer id) {
	//	System.out.println("RestArticleControlle   editing existing article");
		Integer user_id = getCurrentUserID();
    	if(user_id==null){System.out.println("U are not authorised"); return;}	
		articleService.editArticle(articleModel, id, user_id);
	}
	
	@DeleteMapping("/articles/{id}")
	public @ResponseBody void deleteArticle(@PathVariable("id") Integer id) {
		Integer user_id = getCurrentUserID();
    	if(user_id==null) {System.out.println("U are not authorised"); return;}
		articleService.deleteArticle(id, user_id);
	}
	
    @GetMapping("/my")
	public List<ArticleModel> getAllCurrentUserArticles() {
    	Integer user_id = getCurrentUserID();
    	if(user_id==null) {System.out.println("U are not authorised"); return null;}
    	return articleService.getAllCurrentUserArticles(user_id);
	}
	
    public Integer getCurrentUserID() {
		String token = request.getHeader("Authorization");
		if(token==null) {return null;}
		String userEmail = jwtProvider.getEmailFromToken(token);
		if(userEmail==null) return null;	
		return userService.findByEmail(userEmail).getId();
    }
}
