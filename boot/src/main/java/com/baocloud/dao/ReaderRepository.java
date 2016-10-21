package com.baocloud.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baocloud.entity.Reader;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
	
	Reader findByUsernameAndPassword(String username,String password);
   
}
