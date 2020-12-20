package com.jtrack.service;

import java.util.List;

import com.jtrack.dto.LoginHistResp;

public interface LoginHistService {

	List<LoginHistResp> getLoginHistList();
	
	LoginHistResp addLoginHist(String userId);
}
