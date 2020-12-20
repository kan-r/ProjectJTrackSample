package com.jtrack.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.JobReq;
import com.jtrack.dto.JobResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.JobService;

@RestController
@RequestMapping("/jobs")
public class JobController {
	
	@Autowired
	private JobService jobService;
	
	@GetMapping("")
	public List<JobResp> getJobList(){
		return jobService.getJobList();
	}
	
	@GetMapping(path="", params = "type")
	public List<JobResp> getJobListByType(@RequestParam String type){
		return jobService.getJobListByType(type);
	}
	
	@GetMapping(path = "", 
			params = {
				"name", 
				"type", 
				"status", 
				"assignedTo",
				"includeChild",
				"nameC", 
				"typeC", 
				"statusC", 
				"assignedToC"
			})
	public List<JobResp> getJobListByParams(@RequestParam Map<String,String> params){
		return jobService.getJobListByParams(params);
	}
	
	@GetMapping("/{id}")
	public JobResp getJob(@PathVariable long id){
		return jobService.getJob(id);
	}

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public JobResp addJob(@Valid @RequestBody JobReq jobReq) throws InvalidDataException {
		return jobService.addJob(jobReq);
	}
	
	@PutMapping("/{id}")
	public JobResp updateJob(@PathVariable long id, @RequestBody JobReq jobReq) throws InvalidDataException {
		return jobService.updateJob(id, jobReq);
	}
	
	@DeleteMapping("/{id}")
	public DeleteResp deleteJob(@PathVariable long id) throws InvalidDataException {
		return jobService.deleteJob(id);
	}
}
