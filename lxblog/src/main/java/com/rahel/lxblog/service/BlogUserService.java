package com.rahel.lxblog.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rahel.lxblog.dao.UserDao;
import com.rahel.lxblog.dto.RegistrationRequest;
import com.rahel.lxblog.dto.ResetPasswordForm;
import com.rahel.lxblog.entity.ActivationCode;
import com.rahel.lxblog.entity.BlogUser;
import com.rahel.lxblog.entity.Roles;

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
		if ((registrationRequest.getEmail().isEmpty()) || (registrationRequest.getPassword().isEmpty())) {
			return "invalid e-mail or password";
		}
		if (userDao.findByEmail(registrationRequest.getEmail()).isPresent()) {
			return "user with such e-mail is already exist";
		}
		String code = UUID.randomUUID().toString();
		String message = String.format("http://localhost:8080/auth/confirm/%s", code);
		try {
			mailSender.send(registrationRequest.getEmail(), "registration confirmation", message);
		} catch (MailSendException e) {
			return "Invalid e-mail";
		}
		BlogUser blogUser = new BlogUser(registrationRequest);
		blogUser.setRole_name(Roles.USER.getName());
		blogUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
		blogUser = userDao.save(blogUser);
        acService.setActivationCode(blogUser, code);       
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

	public String activateUser(String code) {
		Optional<ActivationCode> ac = acService.findById(code);
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
			return null;
		}
		return "No such user";
	}

	public String dropPassword(String email) {
		Optional<BlogUser> blogUser = userDao.findByEmail(email);
		if (blogUser.isEmpty()) {
			return "No user with such e-mail";
		}
		blogUser.get().setIs_active(0);
		String code = UUID.randomUUID().toString();
		ActivationCode ac = acService.setActivationCode(blogUser.get(), code);
		System.out.println(ac);
		String message = String.format("password reset code is %s", ac.getId());
		try {
			mailSender.send(blogUser.get().getEmail(), "password reset", message);
		} catch (MailSendException e) {
			return "Invalid e-mail";
		}
		return null;
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
		}
		return null;
	}

}
