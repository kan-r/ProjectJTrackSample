package com.jtrack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtrack.dto.LoginHistResp;
import com.jtrack.service.LoginHistService;

@RestController
@RequestMapping("/loginHist")
public class LoginHistController {

	@Autowired
	LoginHistService loginHistService;

	@GetMapping("")
	public List<LoginHistResp> getLoginHistList(){
		return loginHistService.getLoginHistList();
	}
}
