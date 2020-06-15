package com.tech.mkblogs.user.service;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.tech.mkblogs.response.ErrorObject;
import com.tech.mkblogs.response.ResponseDTO;
import com.tech.mkblogs.user.dto.UserDTO;
import com.tech.mkblogs.user.filter.UserFilterDTO;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
@Transactional
public class UserServiceTest {

	@Autowired
	UserService userService;
	
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
		assertNotNull(userService);
	}
	
	@Test
	@Rollback(false)
	public void testSaveObject() { 
		UserDTO userDTO = UserDTO.builder()
								 .loginName("ServiceLoginname17")
								 .password("second")
								 .firstName("Servicename")
								 .lastName("servicelastname")
								 .role("ROLE_USER")
								 .build();
		System.out.println(userDTO);
		ResponseDTO<UserDTO,ErrorObject> responseDTO = userService.saveUser(userDTO);
		if(responseDTO.getIsError()) {
			log.info(responseDTO.getErrorObject());
		}else {
			log.info(responseDTO.getSuccessObject());
		}						 
	}
	
	@Test
	@Rollback(false)
	public void testUpdateObject() {
		UserDTO userDTO = UserDTO.builder()
								 .loginName("ServiceLoginname27")
								 .password("second")
								 .firstName("Servicename")
								 .lastName("servicelastname")
								 .role("ROLE_USER")
								 .build();
		
		ResponseDTO<UserDTO,ErrorObject> responseDTO = userService.saveUser(userDTO);
		if(responseDTO.getIsError()) {
			log.info(responseDTO.getErrorObject());
		}else {
			userDTO.setRole("ROLE_ADMIN");
			UserDTO dbObjectDTO = responseDTO.getSuccessObject();
			userDTO.setId(dbObjectDTO.getId());
			responseDTO = userService.updateUser(userDTO);
			if(responseDTO.getIsError()) {
				log.info(responseDTO.getErrorObject());
			}else {
				log.info(responseDTO.getSuccessObject());
			}	
		}						 
	}
	
	@Test
	public void testGetObject() {
		Integer userId = 19;
		ResponseDTO<UserDTO,ErrorObject> responseDTO = userService.getUser(userId);
		if(responseDTO.getIsError()) {
			log.info(responseDTO.getErrorObject());
		}else {
			UserDTO userDTO = responseDTO.getSuccessObject();
			log.info(userDTO);	
		}						 
	}
	
	@Test
	public void testGetAllObjects() {
		ResponseDTO<List<UserDTO>,ErrorObject> responseDTO = userService.getAllData();
		if(responseDTO.getIsError()) {
			log.info(responseDTO.getErrorObject());
		}else {
			List<UserDTO> userList = responseDTO.getSuccessObject();
			userList.stream().forEach((dto) -> log.info(dto));
		}						 
	}
	
	@Test
	public void testfilterObjects() {
		UserFilterDTO filterDTO = UserFilterDTO.builder()
				                               .firstName("")
				                               .username("firstmysql")
				                               .lastName("")
				                               .build();
		ResponseDTO<List<UserDTO>,ErrorObject> responseDTO = userService.search(filterDTO);
		if(responseDTO.getIsError()) {
			log.info(responseDTO.getErrorObject());
		}else {
			List<UserDTO> userList = responseDTO.getSuccessObject();
			userList.stream().forEach((dto) -> log.info(dto));
		}						 
	}
}
