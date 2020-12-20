package com.jtrack.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtrack.dto.JobPriorityReq;
import com.jtrack.dto.JobPriorityResp;
import com.jtrack.model.JobPriority;

@Component
public class JobPriorityMapper {
	
	@Autowired
    private ModelMapper modelMapper;

	public JobPriority convertToModel(JobPriorityReq jobPriorityReq) {
		if(jobPriorityReq == null) {
			return null;
		}
		
        return modelMapper.map(jobPriorityReq, JobPriority.class);
    }
	
	public JobPriority convertToModel(JobPriorityReq jobPriorityReq, JobPriority jobPriority) {
		if(jobPriorityReq == null || jobPriority == null) {
			return null;
		}
		
		modelMapper.map(jobPriorityReq, jobPriority);
		return jobPriority;
	}
	
	public JobPriorityResp convertToDto(JobPriority jobPriority) {
		if(jobPriority == null) {
			return null;
		}
		
		return modelMapper.map(jobPriority, JobPriorityResp.class);
    }
	
	public List<JobPriorityResp> convertToDto(List<JobPriority> jobPriorityList){
		if(jobPriorityList == null) {
			return null;
		}
		
		return jobPriorityList
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}
}
