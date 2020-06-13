package com.rahel.lxblog.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rahel.lxblog.entity.Comment;

@Repository
public interface CommentDao extends JpaRepository<Comment, Integer> {

	@Modifying
	Comment save(Comment comment);

	Optional<Comment> findById(Integer article_id);

	@Modifying
	void deleteById(Integer id);

	@Query("SELECT c FROM Comment c WHERE post_id=?1")
	List<Comment> findAllByPost_id(Integer post_id);

	@Query("SELECT c FROM Comment c WHERE post_id=?1")
	ArrayList<Comment> findAllByPost_id(Integer article_id, Pageable pageable);

}
