package com.jtrack.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.UserReq;
import com.jtrack.dto.UserReqUpd;
import com.jtrack.dto.UserResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;

	@GetMapping(path="")
	public List<UserResp> getUserList(){
		return userService.getUserList();
	}
	
	@GetMapping("/{id}")
	public UserResp getUser(@PathVariable String id){
		return userService.getUser(id);
	}

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public UserResp addUser(@Valid @RequestBody UserReq userReq) throws InvalidDataException {
		return userService.addUser(userReq);
	}
	
	@PutMapping("/{id}")
	public UserResp updateUser(@PathVariable String id, @Valid @RequestBody UserReqUpd userReqUpd) throws InvalidDataException {
		return userService.updateUser(id, userReqUpd);
	}
	
	@DeleteMapping("/{id}")
	public DeleteResp deleteUser(@PathVariable String id) throws InvalidDataException {
		return userService.deleteUser(id);
	}
}
