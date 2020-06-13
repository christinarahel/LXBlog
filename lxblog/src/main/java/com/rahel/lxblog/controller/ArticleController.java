package com.rahel.lxblog.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.rahel.lxblog.entity.BlogUser;
import com.rahel.lxblog.model.ArticleRequest;
import com.rahel.lxblog.model.ArticleResponse;
import com.rahel.lxblog.service.ArticleService;
import com.rahel.lxblog.service.BlogUserService;

@RestController
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private BlogUserService userService;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private HttpServletRequest request;
	
	@GetMapping("/articles")
	public List<ArticleResponse> getAllPublicArticles() {	
		return articleService.getAllPublicArticles();
	}
	
	@PostMapping("/articles") 
	public void createArticle(@RequestBody ArticleRequest articleRequest) {
		Integer user_id = getCurrentUserID();
    	if(user_id==null) {System.out.println("U are not authorised"); return;}
		articleService.saveArticle(articleRequest, user_id);
	}
	
	// /articles?skip=0&limit=10&q=post_title&author=id&sort=field_name&order=asc|desc
	//@GetMapping(value ="/articles", params= {"skip","limit","author","sort","order"})
	//public List<ArticleResponse> getRequeredPublicArticles(@PathVariable("skip") Integer skip, @PathVariable("limit") Integer limit, @PathVariable("author") Integer author,
			//@PathVariable("sort") String sort, @PathVariable("order") String order) 
@GetMapping(value ="/articles", params= {"skip","limit","author","sort","order"})
public List<ArticleResponse> getRequeredPublicArticles(@RequestParam Map<String, String> requestParams){	
	    Integer skip =  Integer.valueOf(requestParams.get("skip"));
	    Integer limit =  Integer.valueOf(requestParams.get("limit"));
	    Integer author = Integer.valueOf(requestParams.get("author"));
	    String sort =  requestParams.get("sort");
	    String order =  requestParams.get("order");
	   
		System.out.println(skip +" " + limit +" "+ author +" " + sort+ "  "+ order);
		
	//	return null;
		return articleService.getRequeredPublicArticles(skip,limit,author,sort,order);
	}
	
	@PutMapping("/articles/{id}")
	public void editArticle(@RequestBody ArticleRequest articleRequest, @PathVariable("id") Integer id) {
		Integer user_id = getCurrentUserID();
    	if(user_id==null){System.out.println("U are not authorised"); return;}	
		articleService.editArticle(articleRequest, id, user_id);
	}
	
	@DeleteMapping("/articles/{id}")
	public @ResponseBody void deleteArticle(@PathVariable("id") Integer id) {
		Integer user_id = getCurrentUserID();
    	if(user_id==null) {System.out.println("U are not authorised"); return;}
		articleService.deleteArticle(id, user_id);
	}
	
    @GetMapping("/my")
	public List<ArticleResponse> getAllCurrentUserArticles() {
    	Integer user_id = getCurrentUserID();
    	if(user_id==null) {System.out.println("U are not authorised"); return null;}
    	return articleService.getAllCurrentUserArticles(user_id);
	}
	
    public Integer getCurrentUserID() {
		String token = request.getHeader("Authorization");
		if(token==null) {return null;}
		String userEmail = jwtProvider.getEmailFromToken(token);
		if(userEmail==null) return null;	
		Optional<BlogUser> blogUser = userService.findByEmail(userEmail);
		if(blogUser.isPresent()) return blogUser.get().getId();
		return null;
    }
}
