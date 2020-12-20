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
import com.jtrack.dto.JobStatusReq;
import com.jtrack.dto.JobStatusResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.JobStatusMapper;
import com.jtrack.model.JobStatus;
import com.jtrack.repository.JobStatusRepository;
import com.jtrack.util.GenUtils;
import com.jtrack.validation.ValidationUtils;

@Service
@Transactional
public class JobStatusServiceImpl implements JobStatusService {
	
	Logger logger = LogManager.getLogger(JobStatusServiceImpl.class);
	
	@Autowired
	private JobStatusRepository jobStatusRepository;
	
	@Autowired
	private JobStatusMapper mapper;

	private List<JobStatus> getAll(){
		logger.info("getAll()");
		return jobStatusRepository.findAll(Sort.by("jobStatus"));
	}
	
	private JobStatus get(String jobStatusId){
		logger.info("get({})", jobStatusId);
		Optional<JobStatus> jobStatus = jobStatusRepository.findById(jobStatusId);
		if(jobStatus.isPresent()) {
			return jobStatus.get();
		}
		
		return null;
	}
	
	private JobStatus add(JobStatus jobStatus) throws InvalidDataException {
		logger.info("add({})", jobStatus);
		
		if(jobStatusExists(jobStatus.getJobStatus())) {
			throw new InvalidDataException(
					ValidationUtils.jobStatusExistsMsg(jobStatus.getJobStatus())
			);
		}
		
		jobStatus.setUserCrt(GenUtils.getCurrentUserId());
		jobStatus.setDateCrt(LocalDateTime.now());
		 
	    return jobStatusRepository.save(jobStatus);
	}
	
	private JobStatus update(String jobStatusId, JobStatus jobStatus) throws InvalidDataException {
		logger.info("update({}, {})", jobStatusId, jobStatus);
		
		if(!jobStatusExists(jobStatusId)) {
			throw new InvalidDataException(
					ValidationUtils.jobStatusDoesNotExistMsg(jobStatusId)
			);
		}
		
		jobStatus.setUserMod(GenUtils.getCurrentUserId());
		jobStatus.setDateMod(LocalDateTime.now());
		
		return jobStatusRepository.save(jobStatus);
	}
	
	private void delete(String jobStatusId) throws InvalidDataException {
		logger.info("delete({})", jobStatusId);
		
		if(!jobStatusExists(jobStatusId)) {
			throw new InvalidDataException(
					ValidationUtils.jobStatusDoesNotExistMsg(jobStatusId)
			);
		}
		
		jobStatusRepository.deleteById(jobStatusId);
	}
	
	private boolean jobStatusExists(String jobStatusId) {
		JobStatus jobStatusExisting = get(jobStatusId);
		return (jobStatusExisting != null);
	}
	
	//Interface Implementations | Conversion to DTO :---

	@Override
	public List<JobStatusResp> getJobStatusList() {
		return mapper.convertToDto(
				getAll()
		);
	}

	@Override
	public JobStatusResp getJobStatus(String jobStatusId) {
		return mapper.convertToDto(
				get(jobStatusId)
		);
	}

	@Override
	public JobStatusResp addJobStatus(JobStatusReq jobStatusReq) throws InvalidDataException {
		return mapper.convertToDto(
				add(mapper.convertToModel(jobStatusReq))
		);
	}

	@Override
	public JobStatusResp updateJobStatus(String jobStatusId, JobStatusReq jobStatusReq) throws InvalidDataException {
		jobStatusReq.setJobStatus(jobStatusId);
		return mapper.convertToDto(
				update(jobStatusId, mapper.convertToModel(jobStatusReq, get(jobStatusId)))
		);
	}

	@Override
	public DeleteResp deleteJobStatus(String jobStatusId) throws InvalidDataException {
		delete(jobStatusId);
		return new DeleteResp(ValidationUtils.jobStatusDeletedMsg(jobStatusId));
	}
}
