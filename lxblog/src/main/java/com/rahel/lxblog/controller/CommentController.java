package com.rahel.lxblog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rahel.lxblog.config.jwt.JwtProvider;
import com.rahel.lxblog.model.CommentRequest;
import com.rahel.lxblog.model.CommentResponse;
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
	
	/*POST /articles/:id/comments - добавить со ссылкой на пост и пользователя

	GET /articles/:id/comments - список комментариев поста

	GET /articles/:id/comments/:id - просмотр комментария

	DELETE /articles/:id/comments/:id - удалить, удалить может только автор */
	

	@GetMapping("/articles/{id}/comments")
	public @ResponseBody List<CommentResponse> showAllCurrentArticleComments(@PathVariable Integer id) {
		return commentService.getAllCurrentArticleComments(id);
	}
	
	@PostMapping("/articles/{id}/comments")
	public void createComment(@RequestBody CommentRequest commentRequest, @PathVariable Integer id) {
		Integer user_id = getCurrentUserID();
		if(user_id == null) {System.out.println("You are not authorised"); return;}
		commentService.saveComment(commentRequest, id, user_id);
		
	}
	
	@GetMapping("/articles/{id}/comments/{comment_id}")
	public @ResponseBody CommentResponse showComment(@PathVariable Integer id, @PathVariable Integer comment_id) {
		return commentService.getComment(comment_id);
	}
	
	@DeleteMapping("/articles/{id}/comments/{comment_id}")
	public void deleteComment(@PathVariable Integer id, @PathVariable Integer comment_id) {
		Integer user_id = getCurrentUserID();
		if(user_id == null) { System.out.println("You are not authorised"); return;}
		else {
			commentService.deleteComment(comment_id, user_id);
		}
	}
	
	public Integer getCurrentUserID() {
		String token = request.getHeader("Authorization");
		if(token==null) {return null;}
		String userEmail = jwtProvider.getEmailFromToken(token);
		if(userEmail==null) return null;	
		return userService.findByEmail(userEmail).getId();
	}
}
