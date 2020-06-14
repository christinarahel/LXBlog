package com.rahel.lxblog.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rahel.lxblog.dto.CommentRequest;
import com.rahel.lxblog.dto.CommentResponse;
import com.rahel.lxblog.jwt.JwtProvider;
import com.rahel.lxblog.service.BlogUserService;
import com.rahel.lxblog.service.CommentService;

@RestController
public class CommentController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private BlogUserService userService;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private HttpServletRequest request;

	@GetMapping("/articles/{id}/comments")
	public @ResponseBody List<CommentResponse> showAllCurrentArticleComments(@PathVariable Integer id) {
		return commentService.getAllCurrentArticleComments(id);
	}

	@PostMapping("/articles/{id}/comments")
	public void createComment(@RequestBody CommentRequest commentRequest, @PathVariable Integer id) {
		Integer user_id = getCurrentUserID();
		commentService.saveComment(commentRequest, id, user_id);
	}

	@GetMapping("/articles/{id}/comments/{comment_id}")
	public @ResponseBody CommentResponse showComment(@PathVariable Integer id, @PathVariable Integer comment_id) {
		return commentService.getComment(comment_id);
	}

	@DeleteMapping("/articles/{id}/comments/{comment_id}")
	public ResponseEntity<String> deleteComment(@PathVariable Integer id, @PathVariable Integer comment_id) {
		Integer user_id = getCurrentUserID();
		String response = commentService.deleteComment(comment_id, user_id);
		if (response == null) {
			return ResponseEntity.ok().body("The comment is deleted");
		} else {
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping(value = "/articles/{id}/comments", params = { "skip", "limit", "sort", "order" })
	public List<CommentResponse> getRequiredComments(@PathVariable Integer id,
			@RequestParam Map<String, String> requestParams) {
		Integer skip = Integer.valueOf(requestParams.get("skip"));
		Integer limit = Integer.valueOf(requestParams.get("limit"));
		String sort = requestParams.get("sort");
		String order = requestParams.get("order");
		return commentService.getRequiredComments(id, skip, limit, sort, order);
	}

	public Integer getCurrentUserID() {
		String token = request.getHeader("Authorization");
		if (token == null) {
			return null;
		}
		String userEmail = jwtProvider.getEmailFromToken(token);
		if (userEmail == null)
			return null;
		return userService.findByEmail(userEmail).get().getId();
	}
}
