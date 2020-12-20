package com.jtrack.service;

import java.util.List;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.JobStageReq;
import com.jtrack.dto.JobStageResp;
import com.jtrack.exception.InvalidDataException;

public interface JobStageService {

	List<JobStageResp> getJobStageList();
	
	JobStageResp getJobStage(String jobStageId);
	
	JobStageResp addJobStage(JobStageReq jobStageReq) throws InvalidDataException;
	
	JobStageResp updateJobStage(String jobStageId, JobStageReq jobStageReq) throws InvalidDataException;
	
	DeleteResp deleteJobStage(String jobStageId) throws InvalidDataException;
}
