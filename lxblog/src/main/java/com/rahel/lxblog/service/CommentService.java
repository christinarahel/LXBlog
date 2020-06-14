package com.rahel.lxblog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rahel.lxblog.dao.CommentDao;
import com.rahel.lxblog.dao.UserDao;
import com.rahel.lxblog.dto.CommentRequest;
import com.rahel.lxblog.dto.CommentResponse;
import com.rahel.lxblog.entity.BlogUser;
import com.rahel.lxblog.entity.Comment;

@Service
@Transactional
public class CommentService {

	@Autowired
	private CommentDao commentDao;

	@Autowired
	private UserDao userDao;

	public List<CommentResponse> getAllCurrentArticleComments(Integer id) {
		ArrayList<Comment> comments = (ArrayList<Comment>) commentDao.findAllByPost_id(id);
		ArrayList<CommentResponse> commentResponses = new ArrayList<>();
		for (int i = 0; i < comments.size(); i++) {
			CommentResponse commentResponse = getCommentResponse(comments.get(i));
			commentResponses.add(commentResponse);
		}
		return commentResponses;
	}

	public void saveComment(CommentRequest commentRequest, Integer article_id, Integer user_id) {
		Comment comment = new Comment();
		comment.setMessage(commentRequest.getMessage());
		comment.setPost_id(article_id);
		comment.setAuthor_id(user_id);
		comment.setCreated_at(new java.sql.Date(new java.util.Date().getTime()));
		commentDao.save(comment);

	}

	public CommentResponse getComment(Integer comment_id) {
		Optional<Comment> comment = commentDao.findById(comment_id);
		if (comment.isEmpty()) {
			return null;
		} else {
			return getCommentResponse(comment.get());
		}
	}

	private CommentResponse getCommentResponse(Comment comment) {
		CommentResponse commentResponse = new CommentResponse();
		commentResponse.setId(comment.getId());
		commentResponse.setMessage(comment.getMessage());
		commentResponse.setCreated_at(new java.util.Date(comment.getCreated_at().getTime()));
		Optional<BlogUser> blogUser = userDao.findById(comment.getAuthor_id());
		if (blogUser.isPresent()) {
			commentResponse.setAuthor_first_name(blogUser.get().getFirst_name());
			commentResponse.setAuthor_last_name(blogUser.get().getLast_name());
		}
		return commentResponse;
	}

	public String deleteComment(Integer comment_id, Integer user_id) {
		Optional<Comment> comment = commentDao.findById(comment_id);
		if (comment.isPresent()) {
			if (comment.get().getAuthor_id() == user_id) {
				commentDao.deleteById(comment_id);
				return null;
			} else
				return "A comment can be deleted by its author only";
		} else
			return "Such comment does not exist";
	}

	public List<CommentResponse> getRequiredComments(Integer article_id, Integer skip, Integer limit, String sort,
			String order) {
		Pageable pageable;
		if (order.equals("asc")) {
			pageable = PageRequest.of(skip, limit, Sort.by(sort).ascending());
		} else {
			pageable = PageRequest.of(skip, limit, Sort.by(sort).descending());
		}

		ArrayList<Comment> comments = (ArrayList<Comment>) commentDao.findAllByPost_id(article_id, pageable);

		ArrayList<CommentResponse> commentResponses = new ArrayList<>();
		for (int i = 0; i < comments.size(); i++) {
			CommentResponse commentResponse = getCommentResponse(comments.get(i));
			commentResponses.add(commentResponse);
		}
		return commentResponses;
	}

}
