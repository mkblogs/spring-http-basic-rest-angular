package com.tech.mkblogs.account.controller;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.tech.mkblogs.account.dto.AccountDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SpringRestClientTest extends BaseControllerTest {

	public static final String REST_SERVICE_URI = "http://localhost:8080/user/api";
	
	RestTemplate restTemplate = new RestTemplate(); 
	
    /*
     * Add HTTP Authorization header, using Basic-Authentication to send user-credentials.
     */
    protected static HttpHeaders getHeaders(){
        String plainCredentials="firstmysql:firstmysql";
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
        String url = REST_SERVICE_URI+"/account";        
        AccountDTO accountDTO = AccountDTO.builder()
				.name("TestAccount1")
				.type("Current")
				.amount(new BigDecimal("100"))
				.build();         
        HttpEntity<AccountDTO> request = new HttpEntity<AccountDTO>(accountDTO,getHeaders());
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        log.info(response.getBody());
    }
    
    @Test
    public void testUpdateObject() {       
        String url = REST_SERVICE_URI+"/account";        
        AccountDTO accountDTO = AccountDTO.builder()
        		.accountId(2)
				.name("TestAccount")
				.type("Current")
				.amount(new BigDecimal("200"))
				.build();         
        HttpEntity<AccountDTO> request = new HttpEntity<AccountDTO>(accountDTO,getHeaders());
        restTemplate.put(url, request);
    }
    
    @Test
    public void testDeleteObject() {
    	HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        String url = REST_SERVICE_URI+"/account/3";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
        log.info(response.getBody());
    }
	
	@Test
    public void getAllData() {
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        String url = REST_SERVICE_URI+"/account";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        log.info(response.getBody());
        
       /* ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, ResponseDTO.class);
        if(responseEntity.getBody().getIsError()) {
        	ResponseDTO<AccountDTO, List<ErrorObject>> responseDTO = responseEntity.getBody();
        	List<ErrorObject> errorList = responseDTO.getErrorObject();
        	errorList.stream().forEach((error) -> log.info(error));
        }else {
        	ResponseDTO<List<AccountDTO>, ErrorObject> responseDTO = responseEntity.getBody();
        	List<AccountDTO> list = responseDTO.getSuccessObject();
        	list.stream().forEach((error) -> log.info(error));
        }
        */
    }
	
	@Test
    public void getFilterData() {
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        String url = REST_SERVICE_URI+"/filterData?accountName=test&accountType=&amount=";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        log.info(response.getBody());
        
       /* ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, ResponseDTO.class);
        if(responseEntity.getBody().getIsError()) {
        	ResponseDTO<AccountDTO, List<ErrorObject>> responseDTO = responseEntity.getBody();
        	List<ErrorObject> errorList = responseDTO.getErrorObject();
        	errorList.stream().forEach((error) -> log.info(error));
        }else {
        	ResponseDTO<List<AccountDTO>, ErrorObject> responseDTO = responseEntity.getBody();
        	List<AccountDTO> list = responseDTO.getSuccessObject();
        	list.stream().forEach((error) -> log.info(error));
        }
        */
    }
}
