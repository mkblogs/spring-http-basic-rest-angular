package com.tech.mkblogs.user.controller;

import java.util.Arrays;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.tech.mkblogs.user.dto.UserDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserSpringRestClientTest {

	public static final String REST_SERVICE_URI = "http://localhost:8080/admin/api";
	
	RestTemplate restTemplate = new RestTemplate(); 
	
    /*
     * Add HTTP Authorization header, using Basic-Authentication to send user-credentials.
     */
    protected static HttpHeaders getHeaders(){
        String plainCredentials="firstmysqladmin:firstmysqladmin";
        String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
        System.out.println(base64Credentials); 
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }
    
    @Test
    public void testSaveObject() {       
        String url = REST_SERVICE_URI+"/user";        
        UserDTO userDTO = UserDTO.builder()
				 .loginName("testboot2")
				 .password("password")
				 .repeatPassword("password")
				 .firstName("Servicename")
				 .lastName("servicelastname")
				 .role("ROLE_USER")
				 .build();
        
        HttpEntity<UserDTO> request = new HttpEntity<UserDTO>(userDTO,getHeaders());
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        log.info(response.getBody());
    }
    
   
}
