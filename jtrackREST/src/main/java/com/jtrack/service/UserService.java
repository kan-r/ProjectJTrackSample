package com.jtrack.service;

import java.util.List;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.UserReq;
import com.jtrack.dto.UserReqUpd;
import com.jtrack.dto.UserResp;
import com.jtrack.exception.InvalidDataException;

public interface UserService {
	
	List<UserResp> getUserList();
	
	UserResp getUser(String userId);
	
	UserResp addUser(UserReq userReq) throws InvalidDataException;
	
	UserResp updateUser(String userId, UserReqUpd userReqUpd) throws InvalidDataException;
	
	DeleteResp deleteUser(String userId) throws InvalidDataException;
}
