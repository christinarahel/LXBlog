package com.rahel.lxblog.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.rahel.lxblog.entity.Tag;

@Repository
public interface TagDao extends JpaRepository<Tag, Integer> {

	Optional<Tag> findById(Integer id);

	Optional<Tag> findByName(String name);

	Tag save(Tag tag);
}
