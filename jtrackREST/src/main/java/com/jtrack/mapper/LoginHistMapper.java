package com.jtrack.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtrack.dto.LoginHistResp;
import com.jtrack.model.LoginHist;

@Component
public class LoginHistMapper {
	
	@Autowired
    private ModelMapper modelMapper;
	
	public LoginHistResp convertToDto(LoginHist loginHist) {
		if(loginHist == null) {
			return null;
		}
		
		return modelMapper.map(loginHist, LoginHistResp.class);
    }
	
	public List<LoginHistResp> convertToDto(List<LoginHist> loginHistList){
		if(loginHistList == null) {
			return null;
		}
		
		return loginHistList
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}
}
