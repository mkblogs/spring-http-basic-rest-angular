package com.tech.mkblogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.tech.mkblogs.onstartup.DBInit;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
@EnableJpaAuditing
public class HttpBasicRestCombineApplication implements CommandLineRunner{

	@Autowired
	DBInit dbInit;
	
	public static void main(String[] args) {
		SpringApplication.run(HttpBasicRestCombineApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("In side run method ");
		dbInit.loadUsers();
	}
}
