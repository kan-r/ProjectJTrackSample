package com.jtrack.service;

import java.util.List;
import java.util.Map;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.JobReq;
import com.jtrack.dto.JobResp;
import com.jtrack.exception.InvalidDataException;

public interface JobService {

	List<JobResp> getJobList();
	
	List<JobResp> getJobListByType(String jobType);
	
	List<JobResp> getJobListByParams(Map<String,String> params);
	
	JobResp getJob(long jobNo);
	
	JobResp addJob(JobReq jobReq) throws InvalidDataException;
	
	JobResp updateJob(long jobNo, JobReq jobReq) throws InvalidDataException;
	
	DeleteResp deleteJob(long jobNo) throws InvalidDataException;
}
