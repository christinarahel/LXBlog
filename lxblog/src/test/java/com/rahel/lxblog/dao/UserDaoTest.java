package com.rahel.lxblog.dao;


import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rahel.lxblog.entity.BlogUser;

import prototype.BlogUserPrototype;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserDaoTest extends Assert{

	@Autowired 
	private UserDao userDao;
	
	@Test
	void testFindByEmail() {
		userDao.save(BlogUserPrototype.aUser());
		BlogUser u = userDao.findByEmail(BlogUserPrototype.aUser().getEmail()).orElse(null);
		assertNotNull(u);
		assertTrue(u.getFirst_name().equals(BlogUserPrototype.aUser().getFirst_name())); 
	}

	@Test
	void testFindByIdInteger() {
		BlogUser u = new BlogUser();
		u.setFirst_name("first_name");
		u.setEmail("test_email");
		BlogUser uSaved = userDao.save(u);
		BlogUser uFoundById = userDao.findById(uSaved.getId()).orElse(null);
		assertNotNull(uFoundById);
		assertTrue(u.getFirst_name().equals(uFoundById.getFirst_name()));
	}

	@Test
	void testSaveBlogUser() {
		BlogUser u = new BlogUser();
		u.setId(2);
		u.setFirst_name("first_name");
		u.setEmail("test_email");
		userDao.save(u);
		BlogUser uFound = userDao.findByEmail(u.getEmail()).orElse(null);
		assertNotNull(uFound);
		assertTrue(u.getFirst_name().equals(uFound.getFirst_name()));
	
	}

}
