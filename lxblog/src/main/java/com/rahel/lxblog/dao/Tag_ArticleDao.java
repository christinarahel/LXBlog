package com.rahel.lxblog.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.rahel.lxblog.entity.Tag_Article;

public interface Tag_ArticleDao extends JpaRepository <Tag_Article, Integer>{

	@Query("SELECT t FROM Tag_Article t where article_id=?1") 
	List<Tag_Article> findAllByArticle(Integer article_id);
	
	@Query("SELECT t FROM Tag_Article t where tag_id=?1") 
	List<Tag_Article> findAllByTag(Integer tag_id);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Tag_Article t where article_id=?1") 
	void deleteAllByArticle(Integer article_id);
	
	//List<Tag_Article> 
	Tag_Article save(Tag_Article ta);
}
