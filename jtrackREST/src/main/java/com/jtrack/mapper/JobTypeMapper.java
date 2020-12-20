package com.jtrack.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtrack.dto.JobTypeReq;
import com.jtrack.dto.JobTypeResp;
import com.jtrack.model.JobType;

@Component
public class JobTypeMapper {
	
	@Autowired
    private ModelMapper modelMapper;

	public JobType convertToModel(JobTypeReq jobTypeReq) {
		if(jobTypeReq == null) {
			return null;
		}
		
        return modelMapper.map(jobTypeReq, JobType.class);
    }
	
	public JobType convertToModel(JobTypeReq jobTypeReq, JobType jobType) {
		if(jobTypeReq == null || jobType == null) {
			return null;
		}
		
		modelMapper.map(jobTypeReq, jobType);
		return jobType;
	}
	
	public JobTypeResp convertToDto(JobType jobType) {
		if(jobType == null) {
			return null;
		}
		
		return modelMapper.map(jobType, JobTypeResp.class);
    }
	
	public List<JobTypeResp> convertToDto(List<JobType> jobTypeList){
		if(jobTypeList == null) {
			return null;
		}
		
		return jobTypeList
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}
}
