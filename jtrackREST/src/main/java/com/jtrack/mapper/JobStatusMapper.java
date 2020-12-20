package com.jtrack.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtrack.dto.JobStatusReq;
import com.jtrack.dto.JobStatusResp;
import com.jtrack.model.JobStatus;

@Component
public class JobStatusMapper {
	
	@Autowired
    private ModelMapper modelMapper;

	public JobStatus convertToModel(JobStatusReq jobStatusReq) {
		if(jobStatusReq == null) {
			return null;
		}
		
        return modelMapper.map(jobStatusReq, JobStatus.class);
    }
	
	public JobStatus convertToModel(JobStatusReq jobStatusReq, JobStatus jobStatus) {
		if(jobStatusReq == null || jobStatus == null) {
			return null;
		}
		
		modelMapper.map(jobStatusReq, jobStatus);
		return jobStatus;
	}
	
	public JobStatusResp convertToDto(JobStatus jobStatus) {
		if(jobStatus == null) {
			return null;
		}
		
		return modelMapper.map(jobStatus, JobStatusResp.class);
    }
	
	public List<JobStatusResp> convertToDto(List<JobStatus> jobStatusList){
		if(jobStatusList == null) {
			return null;
		}
		
		return jobStatusList
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}
}
