package com.rahel.lxblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rahel.lxblog.dao.RoleDao;
import com.rahel.lxblog.dao.UserDao;
import com.rahel.lxblog.entity.BlogUser;
import com.rahel.lxblog.entity.Role;
import com.rahel.lxblog.entity.Roles;

@Service
@Transactional
public class BlogUserService{
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	

	public BlogUser save(BlogUser blogUser) {
	//	Role role = roleDao.findByName(Roles.USER.role());
		blogUser.setRole_name(Roles.USER.role()); //to exchange  USER ROLE ID IS 2
		blogUser.setPassword(passwordEncoder.encode(blogUser.getPassword()));
		return userDao.save(blogUser);
	//	return blogUser;
	}
	
	public BlogUser findByEmail(String email) {
		return userDao.findByEmail(email);
	}
	
	public BlogUser findByEmailAndPassword(String email, String password) {
		BlogUser blogUser = findByEmail(email);
		if(blogUser!=null){
			if(passwordEncoder.matches(password, blogUser.getPassword())) {
				return blogUser;
			}
		}
		return null;
	}

//	public void getUserIdByEmail(String userEmail) {
		// TODO Auto-generated method stub
		
	//}
	

}
