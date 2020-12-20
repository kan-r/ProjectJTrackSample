package com.jtrack.service;

import java.util.List;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.JobStatusReq;
import com.jtrack.dto.JobStatusResp;
import com.jtrack.exception.InvalidDataException;

public interface JobStatusService {

	List<JobStatusResp> getJobStatusList();
	
	JobStatusResp getJobStatus(String jobStatusId);
	
	JobStatusResp addJobStatus(JobStatusReq jobStatusReq) throws InvalidDataException;
	
	JobStatusResp updateJobStatus(String jobStatusId, JobStatusReq jobStatusReq) throws InvalidDataException;
	
	DeleteResp deleteJobStatus(String jobStatusId) throws InvalidDataException;
}
