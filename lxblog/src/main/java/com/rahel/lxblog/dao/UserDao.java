package com.rahel.lxblog.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahel.lxblog.entity.BlogUser;

@Repository
public interface UserDao extends JpaRepository<BlogUser, Integer>{

	Optional<BlogUser> findByEmail(String email);
//	BlogUser findByActivationCode(String code);
	Optional<BlogUser> findById(Integer id);
    BlogUser save(BlogUser user);	
}
