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
import com.jtrack.dto.JobPriorityReq;
import com.jtrack.dto.JobPriorityResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.JobPriorityMapper;
import com.jtrack.model.JobPriority;
import com.jtrack.repository.JobPriorityRepository;
import com.jtrack.util.GenUtils;
import com.jtrack.validation.ValidationUtils;

@Service
@Transactional
public class JobPriorityServiceImpl implements JobPriorityService {
	
	Logger logger = LogManager.getLogger(JobPriorityServiceImpl.class);
	
	@Autowired
	private JobPriorityRepository jobPriorityRepository;
	
	@Autowired
	private JobPriorityMapper mapper;

	private List<JobPriority> getAll(){
		logger.info("getAll()");
		return jobPriorityRepository.findAll(Sort.by("jobPriority"));
	}
	
	private JobPriority get(String jobPriorityId){
		logger.info("get({})", jobPriorityId);
		Optional<JobPriority> jobPriority = jobPriorityRepository.findById(jobPriorityId);
		if(jobPriority.isPresent()) {
			return jobPriority.get();
		}
		
		return null;
	}
	
	private JobPriority add(JobPriority jobPriority) throws InvalidDataException {
		logger.info("add({})", jobPriority);
		
		if(jobPriorityExists(jobPriority.getJobPriority())) {
			throw new InvalidDataException(
					ValidationUtils.jobPriorityExistsMsg(jobPriority.getJobPriority())
			);
		}
		
		jobPriority.setUserCrt(GenUtils.getCurrentUserId());
		jobPriority.setDateCrt(LocalDateTime.now());
		 
	    return jobPriorityRepository.save(jobPriority);
	}
	
	private JobPriority update(String jobPriorityId, JobPriority jobPriority) throws InvalidDataException {
		logger.info("update({}, {})", jobPriorityId, jobPriority);
		
		if(!jobPriorityExists(jobPriorityId)) {
			throw new InvalidDataException(
					ValidationUtils.jobPriorityDoesNotExistMsg(jobPriorityId)
			);
		}

		jobPriority.setUserMod(GenUtils.getCurrentUserId());
		jobPriority.setDateMod(LocalDateTime.now());
		
		return jobPriorityRepository.save(jobPriority);
	}
	
	private void delete(String jobPriorityId) throws InvalidDataException {
		logger.info("delete({})", jobPriorityId);
		
		if(!jobPriorityExists(jobPriorityId)) {
			throw new InvalidDataException(
					ValidationUtils.jobPriorityDoesNotExistMsg(jobPriorityId)
			);
		}
		
		jobPriorityRepository.deleteById(jobPriorityId);
	}
	
	private boolean jobPriorityExists(String jobPriorityId) {
		JobPriority jobPriorityExisting = get(jobPriorityId);
		return (jobPriorityExisting != null);
	}
	
	//Interface Implementations | Conversion to DTO :---

	@Override
	public List<JobPriorityResp> getJobPriorityList() {
		return mapper.convertToDto(
				getAll()
		);
	}

	@Override
	public JobPriorityResp getJobPriority(String jobPriorityId) {
		return mapper.convertToDto(
				get(jobPriorityId)
		);
	}

	@Override
	public JobPriorityResp addJobPriority(JobPriorityReq jobPriorityReq) throws InvalidDataException {
		return mapper.convertToDto(
				add(mapper.convertToModel(jobPriorityReq))
		);
	}

	@Override
	public JobPriorityResp updateJobPriority(String jobPriorityId, JobPriorityReq jobPriorityReq) throws InvalidDataException {
		jobPriorityReq.setJobPriority(jobPriorityId);
		return mapper.convertToDto(
				update(jobPriorityId, mapper.convertToModel(jobPriorityReq, get(jobPriorityId)))
		);
	}

	@Override
	public DeleteResp deleteJobPriority(String jobPriorityId) throws InvalidDataException {
		delete(jobPriorityId);
		return new DeleteResp(ValidationUtils.jobPriorityDeletedMsg(jobPriorityId));
	}
}
