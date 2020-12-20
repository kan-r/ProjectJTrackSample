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
import com.jtrack.dto.JobPriorityReq;
import com.jtrack.dto.JobPriorityResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.JobPriorityService;

@RestController
@RequestMapping("/jobPriorities")
public class JobPriorityController {

	@Autowired
	private JobPriorityService jobPriorityService;

	@GetMapping("")
	public List<JobPriorityResp> getJobPriorityList(){
		return jobPriorityService.getJobPriorityList();
	}
	
	@GetMapping("/{id}")
	public JobPriorityResp getJobPriority(@PathVariable String id){
		return jobPriorityService.getJobPriority(id);
	}

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public JobPriorityResp addJobPriority(@Valid @RequestBody JobPriorityReq jobPriorityReq) throws InvalidDataException {
		return jobPriorityService.addJobPriority(jobPriorityReq);
	}
	
	@PutMapping("/{id}")
	public JobPriorityResp updateJobPriority(@PathVariable String id, @RequestBody JobPriorityReq jobPriorityReq) throws InvalidDataException {
		return jobPriorityService.updateJobPriority(id, jobPriorityReq);
	}
	
	@DeleteMapping("/{id}")
	public DeleteResp deleteJobPriority(@PathVariable String id) throws InvalidDataException {
		return jobPriorityService.deleteJobPriority(id);
	}
}
