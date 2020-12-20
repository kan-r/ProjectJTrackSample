package com.jtrack.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtrack.dto.JobResolutionReq;
import com.jtrack.dto.JobResolutionResp;
import com.jtrack.model.JobResolution;

@Component
public class JobResolutionMapper {
	
	@Autowired
    private ModelMapper modelMapper;

	public JobResolution convertToModel(JobResolutionReq jobResolutionReq) {
		if(jobResolutionReq == null) {
			return null;
		}
		
        return modelMapper.map(jobResolutionReq, JobResolution.class);
    }
	
	public JobResolution convertToModel(JobResolutionReq jobResolutionReq, JobResolution jobResolution) {
		if(jobResolutionReq == null || jobResolution == null) {
			return null;
		}
		
		modelMapper.map(jobResolutionReq, jobResolution);
		return jobResolution;
	}
	
	public JobResolutionResp convertToDto(JobResolution jobResolution) {
		if(jobResolution == null) {
			return null;
		}
		
		return modelMapper.map(jobResolution, JobResolutionResp.class);
    }
	
	public List<JobResolutionResp> convertToDto(List<JobResolution> jobResolutionList){
		if(jobResolutionList == null) {
			return null;
		}
		
		return jobResolutionList
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}
}
