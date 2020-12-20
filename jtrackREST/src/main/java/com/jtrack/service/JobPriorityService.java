package com.jtrack.service;

import java.util.List;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.JobPriorityReq;
import com.jtrack.dto.JobPriorityResp;
import com.jtrack.exception.InvalidDataException;

public interface JobPriorityService {

	List<JobPriorityResp> getJobPriorityList();
	
	JobPriorityResp getJobPriority(String jobPriorityId);
	
	JobPriorityResp addJobPriority(JobPriorityReq jobPriorityReq) throws InvalidDataException;
	
	JobPriorityResp updateJobPriority(String jobPriorityId, JobPriorityReq jobPriorityReq) throws InvalidDataException;
	
	DeleteResp deleteJobPriority(String jobPriorityId) throws InvalidDataException;
}
