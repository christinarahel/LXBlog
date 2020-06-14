package com.rahel.lxblog.service;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rahel.lxblog.dao.ActivationCodeDao;
import com.rahel.lxblog.entity.ActivationCode;
import com.rahel.lxblog.entity.BlogUser;

@Service
public class ActivationCodeService {

	@Autowired
	private ActivationCodeDao acDao;

	public ActivationCode setActivationCode(BlogUser blogUser, String code) {
		ActivationCode ac = new ActivationCode();
		ac.setUser(blogUser.getId());
		ac.setId(code);
		ac.setExpirationDate(Date.from(new Date().toInstant().plus(Duration.ofHours(24))));
		ac = acDao.save(ac);
		return ac;
	}

	public void deleteCode(ActivationCode activationCode) {
		acDao.delete(activationCode);
	}

	public Optional<ActivationCode> findById(String code) {
		return acDao.findById(code);
	}

}
