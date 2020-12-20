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
import com.jtrack.dto.JobStatusReq;
import com.jtrack.dto.JobStatusResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.JobStatusService;

@RestController
@RequestMapping("/jobStatuses")
public class JobStatusController {

	@Autowired
	JobStatusService jobStatusService;

	@GetMapping("")
	public List<JobStatusResp> getJobStatusList(){
		return jobStatusService.getJobStatusList();
	}
	
	@GetMapping("/{id}")
	public JobStatusResp getJobStatus(@PathVariable String id){
		return jobStatusService.getJobStatus(id);
	}

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public JobStatusResp addJobStatus(@Valid @RequestBody JobStatusReq jobStatusReq) throws InvalidDataException {
		return jobStatusService.addJobStatus(jobStatusReq);
	}
	
	@PutMapping("/{id}")
	public JobStatusResp updateJobStatus(@PathVariable String id, @RequestBody JobStatusReq jobStatusReq) throws InvalidDataException {
		return jobStatusService.updateJobStatus(id, jobStatusReq);
	}
	
	@DeleteMapping("/{id}")
	public DeleteResp deleteJobStatus(@PathVariable String id) throws InvalidDataException {
		return jobStatusService.deleteJobStatus(id);
	}
}
