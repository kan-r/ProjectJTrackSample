package com.jtrack.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtrack.dto.JobStageReq;
import com.jtrack.dto.JobStageResp;
import com.jtrack.model.JobStage;

@Component
public class JobStageMapper {
	
	@Autowired
    private ModelMapper modelMapper;

	public JobStage convertToModel(JobStageReq jobStageReq) {
		if(jobStageReq == null) {
			return null;
		}
		
        return modelMapper.map(jobStageReq, JobStage.class);
    }
	
	public JobStage convertToModel(JobStageReq jobStageReq, JobStage jobStage) {
		if(jobStageReq == null || jobStage == null) {
			return null;
		}
		
		modelMapper.map(jobStageReq, jobStage);
		return jobStage;
	}
	
	public JobStageResp convertToDto(JobStage jobStage) {
		if(jobStage == null) {
			return null;
		}
		
		return modelMapper.map(jobStage, JobStageResp.class);
    }
	
	public List<JobStageResp> convertToDto(List<JobStage> jobStageList){
		if(jobStageList == null) {
			return null;
		}
		
		return jobStageList
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}
}
