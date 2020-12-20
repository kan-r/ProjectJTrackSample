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
import com.jtrack.dto.JobStageReq;
import com.jtrack.dto.JobStageResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.JobStageService;

@RestController
@RequestMapping("/jobStages")
public class JobStageController {

	@Autowired
	private JobStageService jobStageService;

	@GetMapping("")
	public List<JobStageResp> getJobStageList(){
		return jobStageService.getJobStageList();
	}
	
	@GetMapping("/{id}")
	public JobStageResp getJobStage(@PathVariable String id){
		return jobStageService.getJobStage(id);
	}

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public JobStageResp addJobStage(@Valid @RequestBody JobStageReq jobStageReq) throws InvalidDataException {
		return jobStageService.addJobStage(jobStageReq);
	}
	
	@PutMapping("/{id}")
	public JobStageResp updateJobStage(@PathVariable String id, @RequestBody JobStageReq jobStageReq) throws InvalidDataException {
		return jobStageService.updateJobStage(id, jobStageReq);
	}
	
	@DeleteMapping("/{id}")
	public DeleteResp deleteJobStage(@PathVariable String id) throws InvalidDataException {
		return jobStageService.deleteJobStage(id);
	}
}
