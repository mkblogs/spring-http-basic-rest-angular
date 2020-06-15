package com.tech.mkblogs.user.controller;

import java.time.LocalDateTime;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tech.mkblogs.model.User;
import com.tech.mkblogs.response.ErrorObject;
import com.tech.mkblogs.response.ResponseDTO;
import com.tech.mkblogs.session.operations.SessionOperations;
import com.tech.mkblogs.user.dto.UserAuthDTO;
import com.tech.mkblogs.user.dto.UserSessionDTO;
import com.tech.mkblogs.user.mapper.UserMapper;
import com.tech.mkblogs.user.repository.UserRepository;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Log4j2
public class AuthController {
	
	@Autowired
	SessionOperations sessionOperations;
	
	@Autowired
	UserRepository userRepository;

	@PostMapping("/token")
	@Transactional
	public ResponseEntity<ResponseDTO<UserAuthDTO,ErrorObject>> token(
			@RequestParam("loginName") String loginName,
			@RequestParam("password") String password) {
		
		Boolean isUser = false;
		Boolean isAdmin = false;
		Boolean isAuthenticated = false;
		log.info(" token() method ");
		ResponseDTO<UserAuthDTO,ErrorObject> responseDTO = new ResponseDTO<UserAuthDTO,ErrorObject>();
		String plainCredentials = loginName+":"+password;
		String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
		
		User loginUser = userRepository.findByLoginName(loginName);
		
        if(loginUser != null) {
    		UserSessionDTO userDTO = new UserSessionDTO();
    		userDTO = UserMapper.INSTANCE.toSessionUserDTO(loginUser);
    		if(userDTO.getAuthority() != null 
    				&& userDTO.getAuthority().equalsIgnoreCase("ROLE_USER")) {
    			isUser = Boolean.TRUE;
    		}else if("ROLE_ADMIN".equalsIgnoreCase(userDTO.getAuthority())) {
    			isAdmin = Boolean.TRUE;
    		}
    		isAuthenticated = Boolean.TRUE;
    		sessionOperations.storeSession(userDTO);
    		
    		loginUser.setLastLogin(LocalDateTime.now());
        	userRepository.save(loginUser);
    		
    		UserAuthDTO authDTO = UserAuthDTO.builder()
	                 .userName(loginName)
	                 .basicToken(base64Credentials)
	                 .isAuthenticated(isAuthenticated)
	                 .isUser(isUser)
	                 .isAdmin(isAdmin)
	                 .build();
			responseDTO.setIsError(false);
			responseDTO.setSuccessObject(authDTO);
        }else {
        	responseDTO.setIsError(true);
    		responseDTO.setSuccessObject(null);
    		ErrorObject error = new ErrorObject();
    		error.setErrorMsg("Authentiction Failed");
    		responseDTO.setErrorObject(error);
        }
		return ResponseEntity.ok().body(responseDTO);
	}
}
