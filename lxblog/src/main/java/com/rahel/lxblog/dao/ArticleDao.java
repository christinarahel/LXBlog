package com.rahel.lxblog.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahel.lxblog.entity.Article;
import com.rahel.lxblog.entity.Tag_Article;

@Repository
public interface ArticleDao extends JpaRepository<Article, Tag_Article>{

	ArrayList<Article> findAllByStatus(String status);
//	List<Article> findAllByAuthor_id(Integer id);
	List<Article> findAll();	
	Article save(Article article);
	Optional<Article> findById(Integer article_id);
//	Article findByEmail(String email);
}
