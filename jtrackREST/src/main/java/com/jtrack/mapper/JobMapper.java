package com.jtrack.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtrack.dto.JobQueryParams;
import com.jtrack.dto.JobReq;
import com.jtrack.dto.JobResp;
import com.jtrack.model.Job;
import com.jtrack.util.GenUtils;

@Component
public class JobMapper {
	
	@Autowired
    private ModelMapper modelMapper;

	public Job convertToModel(JobReq jobReq) {
		if(jobReq == null) {
			return null;
		}
		
        return modelMapper.map(jobReq, Job.class);
    }
	
	public Job convertToModel(JobReq jobReq, Job job) {
		if(jobReq == null || job == null) {
			return null;
		}
		
		modelMapper.map(jobReq, job);
		return job;
	}
	
	public JobQueryParams convertToDto(Map<String,String> params) {
		if(params == null) {
			return null;
		}
		
		JobQueryParams jobQP = new JobQueryParams();
		
		jobQP.setJobName(params.get("name"));
		jobQP.setJobType(params.get("type"));
		jobQP.setJobStatus(params.get("status"));
		jobQP.setAssignedTo(params.get("assignedTo"));
		jobQP.setIncludeChildJobs(GenUtils.toBoolean(params.get("includeChild")));
		jobQP.setJobNameChild(params.get("nameC"));
		jobQP.setJobTypeChild(params.get("typeC"));
		jobQP.setJobStatusChild(params.get("statusC"));
		jobQP.setAssignedToChild(params.get("assignedToC"));
		
		return jobQP;
    }
	
	public JobResp convertToDto(Job job) {
		if(job == null) {
			return null;
		}
		
		return modelMapper.map(job, JobResp.class);
    }
	
	public List<JobResp> convertToDto(List<Job> jobList){
		if(jobList == null) {
			return null;
		}
		
		return jobList
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}
}
