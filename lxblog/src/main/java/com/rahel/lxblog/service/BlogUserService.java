package com.rahel.lxblog.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rahel.lxblog.dao.UserDao;
import com.rahel.lxblog.dto.RegistrationRequest;
import com.rahel.lxblog.dto.ResetPasswordForm;
import com.rahel.lxblog.entity.ActivationCode;
import com.rahel.lxblog.entity.BlogUser;
//import com.rahel.lxblog.entity.Role;
import com.rahel.lxblog.entity.Roles;
import com.rahel.lxblog.jwt.JwtAuthenticationException;

@Service
@Transactional
public class BlogUserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ActivationCodeService acService;

	@Autowired
	private MailSender mailSender;

	public String save(RegistrationRequest registrationRequest) {
		// saving user to database
		if ((registrationRequest.getEmail() == null)||(registrationRequest.getPassword() == null)) {
		//	System.out.println("registrationRequest.getEmail()==null");
			return "invalid e-mail or password";
		}
		if (userDao.findByEmail(registrationRequest.getEmail()).isPresent()) {
		//	System.out.println("userDao.findByEmail(registrationRequest.getEmail()).isPresent");
		//	System.out.println(userDao.findByEmail(registrationRequest.getEmail()).get());
			return "user with such e-mail is already exist";
		}
		BlogUser blogUser = new BlogUser(registrationRequest);
		blogUser.setRole_name(Roles.USER.getName()); 
		blogUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
		blogUser = userDao.save(blogUser);

		ActivationCode ac = acService.setActivationCode(blogUser);
		System.out.println(ac);

		String message = String.format("http://localhost:8080/auth/confirm/%s", ac.getId());
		mailSender.send(blogUser.getEmail(), "registration confirmation", message);

		return null;
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

	public boolean activateUser(String code) {
		System.out.println(code);
		Optional<ActivationCode> ac = acService.findById(code);
		System.out.println(ac);
		if (ac.isEmpty()) {
			throw new JwtAuthenticationException("Activation code is not valid");
		}
		if (ac.get().getExpirationDate().compareTo(new Date()) < 0) {
			throw new JwtAuthenticationException("Activation code is expired");
		}
		Optional<BlogUser> blogUser = userDao.findById(ac.get().getUser());
		if (blogUser.isPresent()) {
			acService.deleteCode(ac.get());
			blogUser.get().setIs_active(1);
			userDao.save(blogUser.get());
			return true;
		}
		return false;
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
			blogUser.get().setPassword(passwordEncoder.encode(prForm.getNewPassword()));
			userDao.save(blogUser.get());
//			return "User account is activated";
		}
//		return "Such user is not exist";
		return null;
	}

}
