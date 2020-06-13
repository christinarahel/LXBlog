package com.rahel.lxblog.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rahel.lxblog.entity.Article;
import com.rahel.lxblog.entity.Tag_Article;

@Repository
public interface ArticleDao extends JpaRepository<Article, Tag_Article> { // must be Integer

	List<Article> findAllByStatus(String status);

	@Query("SELECT a FROM Article a WHERE author_id=?1")
	List<Article> findAllByAuthor_id(Integer author_id);

	List<Article> findAll();

	Article save(Article article);

	Optional<Article> findById(Integer article_id);

	Integer deleteById(Integer id); // Shall I add annotation?

	ArrayList<Article> findAllByStatus(String status, Pageable pageable);

	//ArrayList<Article> findAllByAuthor(Integer author, Pageable pageable);
	@Query("SELECT a FROM Article a WHERE author_id=?1 and status=?2")
	ArrayList<Article> findAllByAuthor_id(Integer author, String status, Pageable pageable);

}
