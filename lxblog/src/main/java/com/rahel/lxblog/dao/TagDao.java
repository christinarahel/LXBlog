package com.rahel.lxblog.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rahel.lxblog.entity.Tag;

@Repository
public interface TagDao extends JpaRepository<Tag, Integer>{
	//ArrayList<Tag> find
	Optional<Tag> findById(Integer id);
	Optional<Tag> findByName(String name);
	
/*	@Query("INSERT INTO Tag FROM tag_article where article_id=?1") 
	void addIfNotExist(Tag tag);*/
	
	Tag save(Tag tag);
	
/*	@Query("SELECT tag_id FROM tag_article where article_id=?1") 
	List<Integer> findAllTagsIds(Integer artcile_id);*/
	
	
}
