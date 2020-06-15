package com.tech.mkblogs.logging.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tech.mkblogs.model.UserActionLog;

@Repository
public interface LogRepository 
			extends JpaRepository<UserActionLog, Integer> {

	
}
