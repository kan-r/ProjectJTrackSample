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
import com.jtrack.dto.JobTypeReq;
import com.jtrack.dto.JobTypeResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.JobTypeMapper;
import com.jtrack.model.JobType;
import com.jtrack.repository.JobTypeRepository;
import com.jtrack.util.GenUtils;
import com.jtrack.validation.ValidationUtils;

@Service
@Transactional
public class JobTypeServiceImpl implements JobTypeService {
	
	Logger logger = LogManager.getLogger(JobTypeServiceImpl.class);
	
	@Autowired
	private JobTypeRepository jobTypeRepository;
	
	@Autowired
	private JobTypeMapper mapper;

	private List<JobType> getAll(){
		logger.info("getAll()");
		return jobTypeRepository.findAll(Sort.by("jobType"));
	}
	
	private JobType get(String jobTypeId){
		logger.info("get({})", jobTypeId);
		Optional<JobType> jobType = jobTypeRepository.findById(jobTypeId);
		if(jobType.isPresent()) {
			return jobType.get();
		}
		
		return null;
	}
	
	private JobType add(JobType jobType) throws InvalidDataException {
		logger.info("add({})", jobType);
		
		if(jobTypeExists(jobType.getJobType())) {
			throw new InvalidDataException(
					ValidationUtils.jobTypeExistsMsg(jobType.getJobType())
			);
		}
		
		jobType.setUserCrt(GenUtils.getCurrentUserId());
		jobType.setDateCrt(LocalDateTime.now());
		 
	    return jobTypeRepository.save(jobType);
	}
	
	private JobType update(String jobTypeId, JobType jobType) throws InvalidDataException {
		logger.info("update({}, {})", jobTypeId, jobType);
		
		if(!jobTypeExists(jobTypeId)) {
			throw new InvalidDataException(
					ValidationUtils.jobTypeDoesNotExistMsg(jobTypeId)
			);
		}
		
		jobType.setUserMod(GenUtils.getCurrentUserId());
		jobType.setDateMod(LocalDateTime.now());
		
		return jobTypeRepository.save(jobType);
	}
	
	private void delete(String jobTypeId) throws InvalidDataException {
		logger.info("delete({})", jobTypeId);
		
		if(!jobTypeExists(jobTypeId)) {
			throw new InvalidDataException(
					ValidationUtils.jobTypeDoesNotExistMsg(jobTypeId)
			);
		}
		
		jobTypeRepository.deleteById(jobTypeId);
	}
	
	private boolean jobTypeExists(String jobTypeId) {
		JobType jobTypeExisting = get(jobTypeId);
		return (jobTypeExisting != null);
	}
	
	//Interface Implementations | Conversion to DTO :---

	@Override
	public List<JobTypeResp> getJobTypeList() {
		return mapper.convertToDto(
				getAll()
		);
	}

	@Override
	public JobTypeResp getJobType(String jobTypeId) {
		return mapper.convertToDto(
				get(jobTypeId)
		);
	}

	@Override
	public JobTypeResp addJobType(JobTypeReq jobTypeReq) throws InvalidDataException {
		return mapper.convertToDto(
				add(mapper.convertToModel(jobTypeReq))
		);
	}

	@Override
	public JobTypeResp updateJobType(String jobTypeId, JobTypeReq jobTypeReq) throws InvalidDataException {
		jobTypeReq.setJobType(jobTypeId);
		return mapper.convertToDto(
				update(jobTypeId, mapper.convertToModel(jobTypeReq, get(jobTypeId)))
		);
	}

	@Override
	public DeleteResp deleteJobType(String jobTypeId) throws InvalidDataException {
		delete(jobTypeId);
		return new DeleteResp(ValidationUtils.jobTypeDeletedMsg(jobTypeId));
	}
}
