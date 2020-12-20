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
import com.jtrack.dto.JobResolutionReq;
import com.jtrack.dto.JobResolutionResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.JobResolutionService;

@RestController
@RequestMapping("/jobResolutions")
public class JobResolutionController {

	@Autowired
	private JobResolutionService jobResolutionService;

	@GetMapping("")
	public List<JobResolutionResp> getJobResolutionList(){
		return jobResolutionService.getJobResolutionList();
	}
	
	@GetMapping("/{id}")
	public JobResolutionResp getJobResolution(@PathVariable String id){
		return jobResolutionService.getJobResolution(id);
	}

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public JobResolutionResp addJobResolution(@Valid @RequestBody JobResolutionReq jobResolutionReq) throws InvalidDataException {
		return jobResolutionService.addJobResolution(jobResolutionReq);
	}
	
	@PutMapping("/{id}")
	public JobResolutionResp updateJobResolution(@PathVariable String id, @RequestBody JobResolutionReq jobResolutionReq) throws InvalidDataException {
		return jobResolutionService.updateJobResolution(id, jobResolutionReq);
	}
	
	@DeleteMapping("/{id}")
	public DeleteResp deleteJobResolution(@PathVariable String id) throws InvalidDataException {
		return jobResolutionService.deleteJobResolution(id);
	}
}
