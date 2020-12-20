package com.jtrack.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.UserReq;
import com.jtrack.dto.UserReqUpd;
import com.jtrack.dto.UserResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.UserMapper;
import com.jtrack.model.User;
import com.jtrack.repository.UserRepository;
import com.jtrack.util.GenUtils;
import com.jtrack.validation.ValidationUtils;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserMapper mapper;

	private List<User> getAll(){
		logger.info("getAll()");
		return userRepository.findAll(Sort.by("userId"));
	}
	
	private User get(String userId){
		logger.info("get({})", userId);
		Optional<User> user = userRepository.findById(userId);
		if(user.isPresent()) {
			return user.get();
		}
		
		return null;
	}
	
	private User add(User user) throws InvalidDataException {
		logger.info("add({})", user);
		
		if(userExists(user.getUserId())) {
			throw new InvalidDataException(
					ValidationUtils.userExistsMsg(user.getUserId())
			);
		}
		
		user.setUserCrt(GenUtils.getCurrentUserId());
		user.setDateCrt(LocalDateTime.now());
		 
	    return userRepository.save(user);
	}
	
	private void delete(String userId) throws InvalidDataException {
		logger.info("delete({})", userId);
		
		if(!userExists(userId)) {
			throw new InvalidDataException(
					ValidationUtils.userDoesNotExistMsg(userId)
			);
		}
		
		userRepository.deleteById(userId);
	}
	
	private User update(String userId, User user) throws InvalidDataException {
		logger.info("update({}, {})", userId, user);
		
		if(!userExists(userId)) {
			throw new InvalidDataException(
					ValidationUtils.userDoesNotExistMsg(userId)
			);
		}
		
		user.setUserMod(GenUtils.getCurrentUserId());
		user.setDateMod(LocalDateTime.now());
		
		return userRepository.save(user);
	}
	
	private boolean userExists(String userId) {
		User userExisting = get(userId);
		return (userExisting != null);
	}
	
	//Interface Implementations | Conversion to DTO :---

	@Override
	public List<UserResp> getUserList() {
		return mapper.convertToDto(
				getAll()
		);
	}
	
	@Override
	public UserResp getUser(String userId) {
		return mapper.convertToDto(
				get(userId)
		);
	}
	
	@Override
	public UserResp addUser(UserReq userReq) throws InvalidDataException {
		return mapper.convertToDto(
				add(mapper.convertToModel(userReq))
		);
	}
	
	@Override
	public UserResp updateUser(String userId, UserReqUpd userReqUpd) throws InvalidDataException {
		return mapper.convertToDto(
				update(userId, mapper.convertToModel(userReqUpd, get(userId)))
		);
	}
	
	@Override
	public DeleteResp deleteUser(String userId) throws InvalidDataException {
		delete(userId);
		return new DeleteResp(ValidationUtils.userDeletedMsg(userId));
	}
}
