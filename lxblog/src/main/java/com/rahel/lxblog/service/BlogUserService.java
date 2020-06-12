package com.rahel.lxblog.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rahel.lxblog.controller.RegistrationRequest;
import com.rahel.lxblog.dao.RoleDao;
import com.rahel.lxblog.dao.UserDao;
import com.rahel.lxblog.entity.ActivationCode;
import com.rahel.lxblog.entity.BlogUser;
//import com.rahel.lxblog.entity.Role;
import com.rahel.lxblog.entity.Roles;
//import com.rahel.lxblog.entity.UserRedis;
import com.rahel.lxblog.model.ResetPasswordForm;

@Service
@Transactional
public class BlogUserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private ActivationCodeService acService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MailSender mailSender;

	public boolean save(RegistrationRequest registrationRequest) {
		// saving user to database
		if (registrationRequest.getEmail() == null) {
			System.out.println("registrationRequest.getEmail()==null");
			return false;
		}
		if (userDao.findByEmail(registrationRequest.getEmail()).isPresent()) {
			System.out.println("userDao.findByEmail(registrationRequest.getEmail()).isPresent");
			System.out.println(userDao.findByEmail(registrationRequest.getEmail()).get());
			return false;
		}
		BlogUser blogUser = new BlogUser(registrationRequest);
		blogUser.setRole_name(Roles.USER.role()); // to change this
		blogUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
		blogUser = userDao.save(blogUser);

//		sendActivationCode(blogUser);
		ActivationCode ac = acService.setActivationCode(blogUser);
		System.out.println(ac);

		String message = String.format("http://localhost:8080/auth/confirm/%s", ac.getId());
		mailSender.send(blogUser.getEmail(), "registration confirmation", message);

		return true;
	}
	
	public Optional<BlogUser> findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	public BlogUser findByEmailAndPassword(String email, String password) {
		Optional<BlogUser> blogUser = findByEmail(email);
		if (blogUser.isPresent()) {
			if (passwordEncoder.matches(password, blogUser.get().getPassword())) {
				return blogUser.get();
			}
		}
		return null;
	}

	public String activateUser(String code) {
		System.out.println(code);
		Optional<ActivationCode> ac = acService.findById(code);
		System.out.println(ac);
		if (ac.isEmpty()) {
			return "Activation code is not valid";
		}
		if (ac.get().getExpirationDate().compareTo(new Date()) < 0) {
			return "Activation code is expired";
		}
		Optional<BlogUser> blogUser = userDao.findById(ac.get().getUser());
		if (blogUser.isPresent()) {
			acService.deleteCode(ac.get());
			blogUser.get().setIs_active(1);
			userDao.save(blogUser.get());
			return "User account is activated";
		}
		return "Such user is not exist";
	}

	public String dropPassword(String email) {
		Optional<BlogUser> blogUser = userDao.findByEmail(email);
		if (blogUser.isEmpty()) {
			return "No user with such e-mail";
		}
		blogUser.get().setIs_active(0);
		ActivationCode ac = acService.setActivationCode(blogUser.get());
		System.out.println(ac);
		String message = String.format("password reset code is %s", ac.getId());
		mailSender.send(blogUser.get().getEmail(), "password reset", message);
		return "e-mail with password reset code was sent";
	}

	public String resetPassword(ResetPasswordForm prForm) {
		Optional<ActivationCode> ac = acService.findById(prForm.getActivationCode());
		if(ac.isEmpty()) {
			return "Activation code is not valid";			
		}
		if (ac.get().getExpirationDate().compareTo(new Date()) < 0) {
			return "Activation code is expired";
		}
		Optional<BlogUser> blogUser = userDao.findById(ac.get().getUser());
		if (blogUser.isPresent()) {
			acService.deleteCode(ac.get());
			blogUser.get().setIs_active(1);
			blogUser.get().setPassword(passwordEncoder.encode(prForm.getNewPassword()));
			userDao.save(blogUser.get());
			return "User account is activated";
		}
//		return "Such user is not exist";
		return null;
	}

	
}
