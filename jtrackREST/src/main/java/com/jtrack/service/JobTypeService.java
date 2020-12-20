package com.jtrack.service;

import java.util.List;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.JobTypeReq;
import com.jtrack.dto.JobTypeResp;
import com.jtrack.exception.InvalidDataException;

public interface JobTypeService {

	List<JobTypeResp> getJobTypeList();
	
	JobTypeResp getJobType(String jobTypeId);
	
	JobTypeResp addJobType(JobTypeReq jobTypeReq) throws InvalidDataException;
	
	JobTypeResp updateJobType(String jobTypeId, JobTypeReq jobTypeReq) throws InvalidDataException;
	
	DeleteResp deleteJobType(String jobTypeId) throws InvalidDataException;
}
