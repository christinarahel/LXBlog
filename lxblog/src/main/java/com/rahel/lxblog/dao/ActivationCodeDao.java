package com.rahel.lxblog.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rahel.lxblog.entity.ActivationCode;

@Repository
public interface ActivationCodeDao extends CrudRepository<ActivationCode, String>{

	ActivationCode save(ActivationCode activationCode);
	Optional<ActivationCode> findById(String id);
	
//	@geury
//	Optional<ActivationCode> findByCode(String Code);
//	List<ActivationCode> findAllByCode(String code);
//	List<ActivationCode> findAll();
	void delete(ActivationCode activationCode);
//	void deleteById(Integer id);
}
