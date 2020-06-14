package com.rahel.lxblog.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.rahel.lxblog.entity.BlogUser;

@Repository
public interface UserDao extends JpaRepository<BlogUser, Integer> {

	Optional<BlogUser> findByEmail(String email);

	Optional<BlogUser> findById(Integer id);

	@Modifying
	BlogUser save(BlogUser user);
}
