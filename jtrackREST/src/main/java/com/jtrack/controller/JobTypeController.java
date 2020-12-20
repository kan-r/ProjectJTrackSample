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
import com.jtrack.dto.JobTypeReq;
import com.jtrack.dto.JobTypeResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.JobTypeService;

@RestController
@RequestMapping("/jobTypes")
public class JobTypeController {

	@Autowired
	JobTypeService jobTypeService;

	@GetMapping("")
	public List<JobTypeResp> getJobTypeList(){
		return jobTypeService.getJobTypeList();
	}
	
	@GetMapping("/{id}")
	public JobTypeResp getJobType(@PathVariable String id){
		return jobTypeService.getJobType(id);
	}

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public JobTypeResp addJobType(@Valid @RequestBody JobTypeReq jobTypeReq) throws InvalidDataException {
		return jobTypeService.addJobType(jobTypeReq);
	}
	
	@PutMapping("/{id}")
	public JobTypeResp updateJobType(@PathVariable String id, @RequestBody JobTypeReq jobTypeReq) throws InvalidDataException {
		return jobTypeService.updateJobType(id, jobTypeReq);
	}
	
	@DeleteMapping("/{id}")
	public DeleteResp deleteJobType(@PathVariable String id) throws InvalidDataException {
		return jobTypeService.deleteJobType(id);
	}
}
