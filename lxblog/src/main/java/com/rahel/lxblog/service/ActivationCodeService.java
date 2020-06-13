package com.rahel.lxblog.service;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.rahel.lxblog.dao.ActivationCodeDao;
import com.rahel.lxblog.dao.UserDao;
import com.rahel.lxblog.entity.ActivationCode;
import com.rahel.lxblog.entity.BlogUser;



@Service
public class ActivationCodeService {
	

	@Autowired
	private ActivationCodeDao acDao;

	public ActivationCode setActivationCode(BlogUser blogUser) {
		ActivationCode ac = new ActivationCode();
		ac.setUser(blogUser.getId());
		ac.setId(UUID.randomUUID().toString());
		ac.setExpirationDate(Date.from(new Date().toInstant().plus(Duration.ofHours(24))));
		System.out.println(ac);  // to delete
		ac = acDao.save(ac);
		System.out.println(ac);
//		System.out.println(acDao.findById(blogUser.getId()));
//		System.out.println(acDao.findByCode(ac.getCode()));
				
		return ac; //acDao.save(ac);
	}

/*	public Optional<ActivationCode> findByCode(String code) {
		System.out.println(acDao.findAllByCode(code));
		return acDao.findByCode(code);
	}*/

	public void deleteCode(ActivationCode activationCode) {
		acDao.delete(activationCode);	
	}

	public Optional<ActivationCode> findById(String code) {
		return acDao.findById(code);
	}

/*	public List<ActivationCode> findAll() {
		
		return acDao.findAll();
	}*/
	

}
