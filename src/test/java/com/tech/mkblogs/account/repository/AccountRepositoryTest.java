package com.tech.mkblogs.account.repository;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.tech.mkblogs.model.Account;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@EnableJpaAuditing
@Log4j2
public class AccountRepositoryTest {

	@Autowired
	AccountRepository accountRepository;
	
	@Before
    public void beforeEachTest() {
		log.info("==================================================================================");
		log.info("This is executed before each Test");
    }
	@After
    public void afterEachTest() {
		log.info("This is exceuted after each Test");
		log.info("==================================================================================");
    }
	@Test
	public void testRepositoryObject() {
		assertNotNull(accountRepository);
	}
	
	@Test
	public void testSaveObject() {
		Account account = Account.of("Test", "Savings", new BigDecimal(100));
		accountRepository.save(account);
		assertNotNull(account.getId());
	}
	
	@Test
	public void testUpdateObject() {
		Account account = Account.of("Test", "Savings", new BigDecimal(100));
		accountRepository.save(account);
		assertNotNull(account.getId());
		account.setAccountName("updated");
		accountRepository.save(account);
	}
	
	@Test
	public void testCreateDeleteObject() {
		Account account = Account.of("Test", "Savings", new BigDecimal(100));
		accountRepository.save(account);
		assertNotNull(account.getId());
		accountRepository.delete(account);
	}
	
	@Test
	public void testGetAllData() {
		List<Account> list = accountRepository.findAll();
		list.stream().forEach((account) -> log.info(account));
	}
	
}
