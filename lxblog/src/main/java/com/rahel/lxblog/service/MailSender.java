package com.rahel.lxblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSender {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String username;
	
	public void send(String emailTo, String subject, String message) {
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setFrom(username); // to exchange is after
		smm.setTo(emailTo);
		smm.setSubject(subject);
		smm.setText(message);
		javaMailSender.send(smm);
	}
	
}
