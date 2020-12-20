package com.jtrack.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtrack.dto.UserReq;
import com.jtrack.dto.UserReqUpd;
import com.jtrack.dto.UserResp;
import com.jtrack.model.User;

@Component
public class UserMapper {
	
	@Autowired
    private ModelMapper modelMapper;

	public User convertToModel(UserReq userReq) {
		if(userReq == null) {
			return null;
		}
		
        return modelMapper.map(userReq, User.class);
    }
	
	public User convertToModel(UserReqUpd userReqUpd, User user) {
		if(userReqUpd == null || user == null) {
			return null;
		}
		
		modelMapper.map(userReqUpd, user);
		return user;
	}
	
	public UserResp convertToDto(User user) {
		if(user == null) {
			return null;
		}
		
		return modelMapper.map(user, UserResp.class);
    }
	
	public List<UserResp> convertToDto(List<User> userList){
		if(userList == null) {
			return null;
		}
		
		return userList
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}
}
