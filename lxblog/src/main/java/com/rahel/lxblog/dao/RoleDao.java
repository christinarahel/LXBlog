package com.rahel.lxblog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.rahel.lxblog.entity.Role;

@Component
public interface RoleDao extends JpaRepository<Role, Integer> {

	Role findByName(String name);
}
