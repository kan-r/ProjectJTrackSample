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
import com.jtrack.dto.JobStageReq;
import com.jtrack.dto.JobStageResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.JobStageMapper;
import com.jtrack.model.JobStage;
import com.jtrack.repository.JobStageRepository;
import com.jtrack.util.GenUtils;
import com.jtrack.validation.ValidationUtils;

@Service
@Transactional
public class JobStageServiceImpl implements JobStageService {
	
	Logger logger = LogManager.getLogger(JobStageServiceImpl.class);
	
	@Autowired
	private JobStageRepository jobStageRepository;
	
	@Autowired
	private JobStageMapper mapper;

	private List<JobStage> getAll(){
		logger.info("getAll()");
		return jobStageRepository.findAll(Sort.by("jobStage"));
	}
	
	private JobStage get(String jobStageId){
		logger.info("get({})", jobStageId);
		Optional<JobStage> jobStage = jobStageRepository.findById(jobStageId);
		if(jobStage.isPresent()) {
			return jobStage.get();
		}
		
		return null;
	}
	
	private JobStage add(JobStage jobStage) throws InvalidDataException {
		logger.info("add({})", jobStage);
		
		if(jobStageExists(jobStage.getJobStage())) {
			throw new InvalidDataException(
					ValidationUtils.jobStageExistsMsg(jobStage.getJobStage())
			);
		}
		
		jobStage.setUserCrt(GenUtils.getCurrentUserId());
		jobStage.setDateCrt(LocalDateTime.now());
		 
	    return jobStageRepository.save(jobStage);
	}
	
	private JobStage update(String jobStageId, JobStage jobStage) throws InvalidDataException {
		logger.info("update({}, {})", jobStageId, jobStage);
		
		if(!jobStageExists(jobStageId)) {
			throw new InvalidDataException(
					ValidationUtils.jobStageDoesNotExistMsg(jobStageId)
			);
		}
		
		jobStage.setUserMod(GenUtils.getCurrentUserId());
		jobStage.setDateMod(LocalDateTime.now());
		
		return jobStageRepository.save(jobStage);
	}
	
	private void delete(String jobStageId) throws InvalidDataException {
		logger.info("delete({})", jobStageId);
		
		if(!jobStageExists(jobStageId)) {
			throw new InvalidDataException(
					ValidationUtils.jobStageDoesNotExistMsg(jobStageId)
			);
		}
		
		jobStageRepository.deleteById(jobStageId);
	}
	
	private boolean jobStageExists(String jobStageId) {
		JobStage jobStageExisting = get(jobStageId);
		return (jobStageExisting != null);
	}
	
	//Interface Implementations | Conversion to DTO :---

	@Override
	public List<JobStageResp> getJobStageList() {
		return mapper.convertToDto(
				getAll()
		);
	}

	@Override
	public JobStageResp getJobStage(String jobStageId) {
		return mapper.convertToDto(
				get(jobStageId)
		);
	}

	@Override
	public JobStageResp addJobStage(JobStageReq jobStageReq) throws InvalidDataException {
		return mapper.convertToDto(
				add(mapper.convertToModel(jobStageReq))
		);
	}

	@Override
	public JobStageResp updateJobStage(String jobStageId, JobStageReq jobStageReq) throws InvalidDataException {
		jobStageReq.setJobStage(jobStageId);
		return mapper.convertToDto(
				update(jobStageId, mapper.convertToModel(jobStageReq, get(jobStageId)))
		);
	}

	@Override
	public DeleteResp deleteJobStage(String jobStageId) throws InvalidDataException {
		delete(jobStageId);
		return new DeleteResp(ValidationUtils.jobStageDeletedMsg(jobStageId));
	}
}
