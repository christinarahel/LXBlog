package com.rahel.lxblog.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rahel.lxblog.controller.RegistrationRequest;
import com.rahel.lxblog.dao.RoleDao;
import com.rahel.lxblog.dao.UserDao;
import com.rahel.lxblog.entity.BlogUser;
//import com.rahel.lxblog.entity.Role;
import com.rahel.lxblog.entity.Roles;
//import com.rahel.lxblog.entity.UserRedis;

@Service
@Transactional
public class BlogUserService{
	
	@Autowired
	private UserDao userDao;
	
//	@Autowired
//	private RoleDao roleDao;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MailSender mailSender;
	
	public boolean save(RegistrationRequest registrationRequest) {
     	//saving user to database
		BlogUser blogUser = new BlogUser(registrationRequest);		
		blogUser.setRole_name(Roles.USER.role());  // to change this
		blogUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
		blogUser.setActivationCode(UUID.randomUUID().toString());
		blogUser = userDao.save(blogUser);
		
		//generating the code and sending to redis
//		UserRedis userRedis = new UserRedis();
//		userRedis.setUser_id(blogUser.getId());
//		userRedis.setActivation_code(UUID.randomUUID().toString());
		
		//sending e-mail
		if (blogUser.getEmail()!=null) {
			String message = String.format("http://localhost:8080/auth/confirm/%s",blogUser.getActivationCode());
			mailSender.send(blogUser.getEmail(), "registration confirmation", message);
		}
		
		return true;
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

	public boolean activateUser(String code) {
		BlogUser blogUser = userDao.findByActivationCode(code);
		System.out.println("user is found by the code =" + blogUser);
		if(blogUser == null) {
			return false;
			} 
		else {
			blogUser.setActivationCode(null);  //delete from redis after
			blogUser.setIs_active(1); 
			blogUser = userDao.save(blogUser);
			System.out.println("user after activation =" + blogUser);
			return true;
		}
		
	}

//	public void getUserIdByEmail(String userEmail) {
		// TODO Auto-generated method stub
		
	//}
	

}
