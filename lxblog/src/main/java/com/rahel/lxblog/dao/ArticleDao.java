package com.rahel.lxblog.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahel.lxblog.entity.Article;

@Repository
public interface ArticleDao extends JpaRepository<Article, Integer>{

	ArrayList<Article> findAllByStatus(String status);
//	List<Article> findAllByAuthor_id(Integer id);
	List<Article> findAll();	
	Article save(Article article);
	Article findById(Article article);
}
