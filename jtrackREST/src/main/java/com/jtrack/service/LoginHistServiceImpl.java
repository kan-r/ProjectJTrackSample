package com.jtrack.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jtrack.dto.LoginHistResp;
import com.jtrack.mapper.LoginHistMapper;
import com.jtrack.model.LoginHist;
import com.jtrack.repository.LoginHistRepository;
import com.jtrack.util.GenUtils;

@Service
@Transactional
public class LoginHistServiceImpl implements LoginHistService {
	
	Logger logger = LogManager.getLogger(LoginHistService.class);
	
	@Autowired
	private LoginHistRepository loginHistRepository;
	
	@Autowired
	private LoginHistMapper mapper;

	private List<LoginHist> getAll(){
		logger.info("getAll()");
		return loginHistRepository.findAll(Sort.by("dateCrt"));
	}
	
	private LoginHist add(LoginHist loginHist) {
		logger.info("add({})", loginHist);
		
		if(loginHist.getUserId() == null || loginHist.getUserId().trim().isEmpty()) {
			loginHist.setUserId(GenUtils.getCurrentUserId());
		}
		
		loginHist.setDateCrt(LocalDateTime.now());
			
		String dtTimeFormatted = GenUtils.formatDateTime(loginHist.getDateCrt(), "yyyyMMddHHmmssSSS");
        String histId = loginHist.getUserId() + "-" + loginHist.getIpAddr() + "-" + dtTimeFormatted;
  
        loginHist.setHistId(histId);
        
	    return loginHistRepository.save(loginHist);
	}

	@Override
	public List<LoginHistResp> getLoginHistList() {
		return mapper.convertToDto(
				getAll()
		);
	}

	@Override
	public LoginHistResp addLoginHist(String userId) {
		LoginHist loginHist = new LoginHist();
		loginHist.setUserId(userId);
		loginHist.setIpAddr(GenUtils.getClientIpAddress());
		
		return mapper.convertToDto(
				add(loginHist)
		);
	}
}
