package com.jtrack.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.JobResolutionReq;
import com.jtrack.dto.JobResolutionResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.JobResolutionMapper;
import com.jtrack.model.JobResolution;
import com.jtrack.repository.JobResolutionRepository;
import com.jtrack.util.GenUtils;
import com.jtrack.validation.ValidationUtils;

@Service
@Transactional
public class JobResolutionServiceImpl implements JobResolutionService {

	Logger logger = LogManager.getLogger(JobResolutionServiceImpl.class);
	
	@Autowired
	private JobResolutionRepository jobResolutionRepository;
	
	@Autowired
	private JobResolutionMapper mapper;

	private List<JobResolution> getAll(){
		logger.info("getAll()");
		return jobResolutionRepository.findAll(Sort.by("jobResolution"));
	}
	
	private JobResolution get(String jobResolutionId){
		logger.info("get({})", jobResolutionId);
		Optional<JobResolution> jobResolution = jobResolutionRepository.findById(jobResolutionId);
		if(jobResolution.isPresent()) {
			return jobResolution.get();
		}
		
		return null;
	}
	
	private JobResolution add(JobResolution jobResolution) throws InvalidDataException {
		logger.info("add({})", jobResolution);
		
		if(jobResolutionExists(jobResolution.getJobResolution())) {
			throw new InvalidDataException(
					ValidationUtils.jobResolutionExistsMsg(jobResolution.getJobResolution())
			);
		}
		
		jobResolution.setUserCrt(GenUtils.getCurrentUserId());
		jobResolution.setDateCrt(LocalDateTime.now());
		 
	    return jobResolutionRepository.save(jobResolution);
	}
	
	private JobResolution update(String jobResolutionId, JobResolution jobResolution) throws InvalidDataException {
		logger.info("update({}, {})", jobResolutionId, jobResolution);
		
		if(!jobResolutionExists(jobResolutionId)) {
			throw new InvalidDataException(
					ValidationUtils.jobResolutionDoesNotExistMsg(jobResolutionId)
			);
		}
		
		jobResolution.setUserMod(GenUtils.getCurrentUserId());
		jobResolution.setDateMod(LocalDateTime.now());
		
		return jobResolutionRepository.save(jobResolution);
	}
	
	private void delete(String jobResolutionId) throws InvalidDataException {
		logger.info("delete({})", jobResolutionId);
		
		if(!jobResolutionExists(jobResolutionId)) {
			throw new InvalidDataException(
					ValidationUtils.jobResolutionDoesNotExistMsg(jobResolutionId)
			);
		}
		
		jobResolutionRepository.deleteById(jobResolutionId);
	}
	
	private boolean jobResolutionExists(String jobResolutionId) {
		JobResolution jobResolutionExisting = get(jobResolutionId);
		return (jobResolutionExisting != null);
	}
	
	//Interface Implementations | Conversion to DTO :---

	@Override
	public List<JobResolutionResp> getJobResolutionList() {
		return mapper.convertToDto(
				getAll()
		);
	}
	
	@Override
	public JobResolutionResp getJobResolution(String jobResolutionId) {
		return mapper.convertToDto(
				get(jobResolutionId)
		);
	}
	
	@Override
	public JobResolutionResp addJobResolution(JobResolutionReq jobResolutionReq) throws InvalidDataException {
		return mapper.convertToDto(
				add(mapper.convertToModel(jobResolutionReq))
		);
	}
	
	@Override
	public JobResolutionResp updateJobResolution(String jobResolutionId, JobResolutionReq jobResolutionReq) throws InvalidDataException {
		jobResolutionReq.setJobResolution(jobResolutionId);
		return mapper.convertToDto(
				update(jobResolutionId, mapper.convertToModel(jobResolutionReq, get(jobResolutionId)))
		);
	}
	
	@Override
	public DeleteResp deleteJobResolution(String jobResolutionId) throws InvalidDataException {
		delete(jobResolutionId);
		return new DeleteResp(ValidationUtils.jobResolutionDeletedMsg(jobResolutionId));
	}
}
