package com.tech.mkblogs.user.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tech.mkblogs.model.User;
import com.tech.mkblogs.response.ErrorObject;
import com.tech.mkblogs.response.ResponseDTO;
import com.tech.mkblogs.user.dto.ChangePasswordDTO;
import com.tech.mkblogs.user.dto.UserDTO;
import com.tech.mkblogs.user.filter.UserFilterDTO;
import com.tech.mkblogs.user.mapper.UserMapper;
import com.tech.mkblogs.user.repository.UserRepository;
import com.tech.mkblogs.user.repository.UserSpecifications;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Transactional
public class UserService {

	@Autowired
	UserRepository repository;
	
	public ResponseDTO<UserDTO,ErrorObject> saveUser(UserDTO dto){
		log.info("Starting the saveUser() method ");
		ResponseDTO<UserDTO,ErrorObject> responseDTO = new ResponseDTO<UserDTO,ErrorObject>();
		try {
			User user = UserMapper.INSTANCE.toModel(dto);
			if(user.getEncrypted() == null) {
				user.setEncrypted(Boolean.FALSE);
			}
			if(user.getEncrypted()) {
				 BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				 user.setPassword(encoder.encode(user.getPassword()));
			}
			user = repository.save(user);
			dto = UserMapper.INSTANCE.toDTO(user);
			dto.setId(user.getId());
			log.info("Generated Primary Key : " + user.getId());
			responseDTO.setIsError(false);
			responseDTO.setSuccessObject(dto);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setIsError(true);
			ErrorObject errorObject = constructResponseErrorObjct("SAVE_ERROR", e.getMessage());
			responseDTO.setSuccessObject(null);
			responseDTO.setErrorObject(errorObject);
		}
		log.info("Ended the saveUser() method ");
		return responseDTO;
		
	}
	public ResponseDTO<UserDTO,ErrorObject> updateUser(UserDTO dto){
		log.info("Starting the updateUser() method ");
		ResponseDTO<UserDTO,ErrorObject> responseDTO = new ResponseDTO<UserDTO,ErrorObject>();
		try {
			Optional<User> dbOptional = repository.findById(dto.getId());
			if(dbOptional.isPresent()) {
				User dbUser = dbOptional.get();
				dbUser = UserMapper.INSTANCE.toDTOForUpdate(dto, dbUser);
				dbUser = repository.save(dbUser);
				dto = UserMapper.INSTANCE.toDTO(dbUser);
				responseDTO.setIsError(false);
				responseDTO.setSuccessObject(dto);
			}else {
				String errorMsg = ("Entity Not Found " + dto.getId());
				ErrorObject errorObject = constructResponseErrorObjct("UPDATE_ERROR", errorMsg);
				responseDTO.setIsError(true);
				responseDTO.setSuccessObject(null);
				responseDTO.setErrorObject(errorObject);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			ErrorObject errorObject = constructResponseErrorObjct("UPDATE_ERROR", e.getMessage());
			responseDTO.setIsError(true);
			responseDTO.setSuccessObject(null);
			responseDTO.setErrorObject(errorObject);
		}
		log.info("Ended the updateUser() method ");
		return responseDTO;
	}
	public ResponseDTO<UserDTO,ErrorObject> getUser(Integer id){
		log.info("Starting the getUser() method ");
		ResponseDTO<UserDTO,ErrorObject> responseDTO = new ResponseDTO<UserDTO,ErrorObject>();
		try {
			Optional<User> dbObjectExists = repository.findById(id);
			if(dbObjectExists.isPresent()) {
				User dbUser = dbObjectExists.get();
				UserDTO userDTO = UserMapper.INSTANCE.toDTO(dbUser);
				responseDTO.setIsError(false);
				responseDTO.setSuccessObject(userDTO);
			}else {
				String errorMsg = ("Entity Not Found " + id);
				ErrorObject errorObject = constructResponseErrorObjct("GET_ERROR", errorMsg);
				responseDTO.setIsError(true);
				responseDTO.setSuccessObject(null);
				responseDTO.setErrorObject(errorObject);
			}
		} catch (Exception e) {			
			String errorMsg = ("getUser() Exception, cause :: " + e.getMessage());
			ErrorObject errorObject = constructResponseErrorObjct("GET_ERROR", errorMsg);
			responseDTO.setIsError(true);
			responseDTO.setSuccessObject(null);
			responseDTO.setErrorObject(errorObject);
		}
		log.info("Ended the getUser() method ");
		return responseDTO;
		
	}
	public ResponseDTO<String,ErrorObject> deleteUser(Integer userId){
		String result = "";
		ResponseDTO<String,ErrorObject> responseDTO = new ResponseDTO<String,ErrorObject>();
		try {
			log.info("Starting the deleteUser() method ");
			Optional<User> optionalUser = repository.findById(userId);
			if(optionalUser.isPresent()) {
				User user = optionalUser.get();
				repository.delete(user);
				result = "Success";
				responseDTO.setIsError(false);
				responseDTO.setSuccessObject(result);
			}else {
				String errorMsg = ("Entity Not Found " + userId);
				ErrorObject errorObject = constructResponseErrorObjct("DELETE_ERROR", errorMsg);
				responseDTO.setIsError(true);
				responseDTO.setSuccessObject(null);
				responseDTO.setErrorObject(errorObject);
			}
		}catch(Exception e) {			
			String errorMsg = ("deleteUser() Exception, cause :: " + e.getMessage());
			ErrorObject errorObject = constructResponseErrorObjct("DELETE_ERROR", errorMsg);
			responseDTO.setIsError(true);
			responseDTO.setSuccessObject(null);
			responseDTO.setErrorObject(errorObject);
		}
		log.info("Ended the deleteUser() method ");
		return responseDTO;
	}
	
	public ResponseDTO<List<UserDTO>,ErrorObject> findByUserName(String username){
		ResponseDTO<List<UserDTO>,ErrorObject> responseDTO = new ResponseDTO<List<UserDTO>, ErrorObject>();
		List<UserDTO> list = new ArrayList<UserDTO>();
		log.info("Starting the findByUserName() method ");
		try {
			Collection<User> dbList =  repository.findAllByLoginName(username);
			list.addAll(dbList.stream()
					.map(dbUser -> UserMapper.INSTANCE.toDTO(dbUser))
					.collect(Collectors.toList()));
			
			responseDTO.setIsError(false);
			responseDTO.setSuccessObject(list);
			responseDTO.setErrorObject(null);
		} catch (Exception e) {
			String errorMsg = ("findByUserName() Exception, cause :: " + e.getMessage());
			ErrorObject errorObject = constructResponseErrorObjct("FIND_BY_USER_NAME_ERROR", errorMsg);
			responseDTO.setIsError(true);
			responseDTO.setSuccessObject(null);
			responseDTO.setErrorObject(errorObject);
		}
		log.info("Ended the findByUserName() method ");
		return responseDTO;
		
	}
	public ResponseDTO<List<UserDTO>,ErrorObject> getAllData(){
		ResponseDTO<List<UserDTO>,ErrorObject> responseDTO = new ResponseDTO<List<UserDTO>, ErrorObject>();
		List<UserDTO> list = new ArrayList<UserDTO>();
		log.info("Starting the getAllData() method ");
		try {
			List<User> dbList = repository.findAll();
			list.addAll(dbList.stream()
					.map(dbUser -> UserMapper.INSTANCE.toDTO(dbUser))
					.collect(Collectors.toList()));
			
			responseDTO.setIsError(false);
			responseDTO.setSuccessObject(list);
			responseDTO.setErrorObject(null);
		} catch (Exception e) {
			String errorMsg = ("getAllData() Exception, cause :: " + e.getMessage());
			ErrorObject errorObject = constructResponseErrorObjct("GET_ALL_DATA", errorMsg);
			responseDTO.setIsError(true);
			responseDTO.setSuccessObject(null);
			responseDTO.setErrorObject(errorObject);
		}
		log.info("Ended the getAllData() method ");
		return responseDTO;
	}
	public ResponseDTO<List<UserDTO>,ErrorObject> search(UserFilterDTO dto){
		ResponseDTO<List<UserDTO>,ErrorObject> responseDTO = new ResponseDTO<List<UserDTO>, ErrorObject>();
		List<UserDTO> list = new ArrayList<UserDTO>();
		log.info("Starting the search() method ");
		try {
			List<User> dbList = repository.findAll(new UserSpecifications(dto));
			list.addAll(dbList.stream()
					.map(dbUser -> UserMapper.INSTANCE.toDTO(dbUser))
					.collect(Collectors.toList()));
			
			responseDTO.setIsError(false);
			responseDTO.setSuccessObject(list);
			responseDTO.setErrorObject(null);
		} catch (Exception e) {
			String errorMsg = ("search() Exception, cause :: " + e.getMessage());
			ErrorObject errorObject = constructResponseErrorObjct("SEARCH", errorMsg);
			responseDTO.setIsError(true);
			responseDTO.setSuccessObject(null);
			responseDTO.setErrorObject(errorObject);
		}
		log.info("Ended the search() method ");
		return responseDTO;
	}
	
	public ResponseDTO<ChangePasswordDTO,ErrorObject> changePassword(ChangePasswordDTO dto){
		log.info("Starting the changePassword() method ");
		System.out.println(dto);
		ResponseDTO<ChangePasswordDTO,ErrorObject> responseDTO = new ResponseDTO<ChangePasswordDTO,ErrorObject>();
		try {
			Integer userId = dto.getUserId();
			Optional<User> userOptional = repository.findById(userId);
			if(userOptional.isPresent()) {
				User user = userOptional.get();
				System.out.println("encrypted :: "+user.getEncrypted());
				if(dto.getEncrypted() == null) {
					user.setEncrypted(Boolean.FALSE);
				}
				System.out.println("DTo ::"+dto.getEncrypted());
				if(dto.getEncrypted()) {
					 user.setEncrypted(Boolean.TRUE);
					 BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
					 user.setPassword(encoder.encode(dto.getPassword()));
				}else {
					user.setPassword(dto.getPassword());
				}
				user = repository.save(user);
				responseDTO.setIsError(false);
				responseDTO.setSuccessObject(dto);
			}else {
				responseDTO.setIsError(true);
				ErrorObject errorObject = constructResponseErrorObjct("CHANE_PWD_ERROR", "User Record not found");
				responseDTO.setSuccessObject(null);
				responseDTO.setErrorObject(errorObject);
			}			
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setIsError(true);
			ErrorObject errorObject = constructResponseErrorObjct("CHANE_PWD_ERROR", e.getMessage());
			responseDTO.setSuccessObject(null);
			responseDTO.setErrorObject(errorObject);
		}
		log.info("Ended the changePassword() method ");
		return responseDTO;
	}
	
	public ErrorObject constructResponseErrorObjct(String errorCode,String errorMessage) {
		ErrorObject errorObject = new ErrorObject();
		errorObject.setErrorCode(errorCode);
		errorObject.setErrorField(errorCode);
		errorObject.setErrorMsg(errorMessage);
		return errorObject;
	}
}
