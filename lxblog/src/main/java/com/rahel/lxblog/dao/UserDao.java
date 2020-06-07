package com.rahel.lxblog.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.rahel.lxblog.entity.BlogUser;

@Repository
public interface UserDao extends JpaRepository<BlogUser, Integer>{

	BlogUser findByEmail(String email);
	Optional<BlogUser> findById(Integer id);
    BlogUser save(BlogUser user);
	
}
