package com.rahel.lxblog.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rahel.lxblog.dto.ArticleRequest;
import com.rahel.lxblog.dto.ArticleResponse;
import com.rahel.lxblog.entity.BlogUser;
import com.rahel.lxblog.jwt.JwtProvider;
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
		if (user_id != null) {
			articleService.saveArticle(articleRequest, user_id);
		}
	}

	@GetMapping(value = "/articles", params = { "skip", "limit", "author", "sort", "order" })
	public List<ArticleResponse> getRequiredPublicArticles(@RequestParam Map<String, String> requestParams) {
		Integer skip = Integer.valueOf(requestParams.get("skip"));
		Integer limit = Integer.valueOf(requestParams.get("limit"));
		Integer author = Integer.valueOf(requestParams.get("author"));
		String sort = requestParams.get("sort");
		String order = requestParams.get("order");
		return articleService.getRequiredPublicArticles(skip, limit, author, sort, order);
	}

	@PutMapping("/articles/{id}")
	public ResponseEntity<String> editArticle(@RequestBody ArticleRequest articleRequest,
			@PathVariable("id") Integer id) {
		Integer user_id = getCurrentUserID();
		String response = articleService.editArticle(articleRequest, id, user_id);
		if (response == null) {
			return ResponseEntity.ok().body("The article is updated");
		} else {
			return ResponseEntity.badRequest().body(response);
		}
	}

	@DeleteMapping("/articles/{id}")
	public ResponseEntity<String> deleteArticle(@PathVariable("id") Integer id) {
		Integer user_id = getCurrentUserID();
		String response = articleService.deleteArticle(id, user_id);
		if (response == null) {
			return ResponseEntity.ok().body("The article is deleted");
		} else {
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/my")
	public List<ArticleResponse> getAllCurrentUserArticles() {
		Integer user_id = getCurrentUserID();
		return articleService.getAllCurrentUserArticles(user_id);
	}

	public Integer getCurrentUserID() {
		String token = request.getHeader("Authorization");
		if (token == null) {
			return null;
		}
		String userEmail = jwtProvider.getEmailFromToken(token);
		if (userEmail == null)
			return null;
		Optional<BlogUser> blogUser = userService.findByEmail(userEmail);
		if (blogUser.isPresent())
			return blogUser.get().getId();
		return null;
	}
}
