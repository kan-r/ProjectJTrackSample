package com.jtrack.service;

import java.util.List;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.JobResolutionReq;
import com.jtrack.dto.JobResolutionResp;
import com.jtrack.exception.InvalidDataException;

public interface JobResolutionService {

	List<JobResolutionResp> getJobResolutionList();
	
	JobResolutionResp getJobResolution(String jobResolutionId);
	
	JobResolutionResp addJobResolution(JobResolutionReq jobResolutionReq) throws InvalidDataException;
	
	JobResolutionResp updateJobResolution(String jobResolutionId, JobResolutionReq jobResolutionReq) throws InvalidDataException;
	
	DeleteResp deleteJobResolution(String jobResolutionId) throws InvalidDataException;
}
