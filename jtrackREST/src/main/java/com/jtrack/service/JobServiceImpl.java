package com.jtrack.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.JobQueryParams;
import com.jtrack.dto.JobReq;
import com.jtrack.dto.JobResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.JobMapper;
import com.jtrack.model.Job;
import com.jtrack.repository.JobRepository;
import com.jtrack.util.GenUtils;
import com.jtrack.validation.ValidationUtils;

@Service
@Transactional
public class JobServiceImpl implements JobService {
	
	Logger logger = LogManager.getLogger(JobService.class);
	
	@Autowired
	private JobRepository jobRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private JobMapper mapper;

	private List<Job> getAll(){
		logger.info("getAll()");
		return jobRepository.findAll(Sort.by("jobNo"));
	}
	
	private List<Job> getAll(String jobType) {
		logger.info("getAll({})", jobType);
		return jobRepository.findByJobType(jobType, Sort.by("jobNo"));
	}
	
	private List<Job> getChildJobList(Long parentJobNo) {
		logger.info("getChildJobList({})", parentJobNo);
		return jobRepository.findByParentJobNo(parentJobNo, Sort.by("jobNo"));
	}
	
	private List<Job> getChildJobList(Long parentJobNo, JobQueryParams jobQP) {
		
		logger.info("getChildJobList({}, {})", parentJobNo, jobQP);
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Job> criteriaQuery = criteriaBuilder.createQuery(Job.class);
		Root<Job> job = criteriaQuery.from(Job.class);
		
		List<Predicate> predicates = new ArrayList<>();
		
		predicates.add(criteriaBuilder.and(criteriaBuilder.equal(job.get("parentJobNo"), parentJobNo)));
		
		if(!jobQP.getJobNameChild().isEmpty()){
			predicates.add(criteriaBuilder.and(criteriaBuilder.like(job.get("jobName"), jobQP.getJobNameChild())));
        }
		
		if(!jobQP.getJobTypeChild().isEmpty()){
			predicates.add(criteriaBuilder.and(criteriaBuilder.equal(job.get("jobType"), jobQP.getJobTypeChild())));
        }
        
        if(!jobQP.getJobStatusChild().isEmpty()){
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(job.get("jobStatus"), jobQP.getJobStatusChild())));
        }
        
        if(!jobQP.getAssignedToChild().isEmpty()){
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(job.get("assignedTo"), jobQP.getAssignedToChild())));
        }
		
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
        criteriaQuery.orderBy(criteriaBuilder.asc(job.get("jobNo")));
        TypedQuery<Job> query = entityManager.createQuery(criteriaQuery);
        List<Job> result = query.getResultList();
        
		return result;
	}
	
	private List<Job> getAll(JobQueryParams jobQP) {
		
		logger.info("getAll({})", jobQP);
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Job> criteriaQuery = criteriaBuilder.createQuery(Job.class);
		Root<Job> job = criteriaQuery.from(Job.class);
		
		List<Predicate> predicates = new ArrayList<>();
		
		predicates.add(criteriaBuilder.and(criteriaBuilder.isNull(job.get("parentJobNo"))));
		
		if(!jobQP.getJobName().isEmpty()){
			predicates.add(criteriaBuilder.and(criteriaBuilder.like(job.get("jobName"), jobQP.getJobName())));
        }
		
		if(!jobQP.getJobType().isEmpty()){
			predicates.add(criteriaBuilder.and(criteriaBuilder.equal(job.get("jobType"), jobQP.getJobType())));
        }
        
        if(!jobQP.getJobStatus().isEmpty()){
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(job.get("jobStatus"), jobQP.getJobStatus())));
        }
        
        if(!jobQP.getAssignedTo().isEmpty()){
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(job.get("assignedTo"), jobQP.getAssignedTo())));
        }
		
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
        criteriaQuery.orderBy(criteriaBuilder.asc(job.get("jobNo")));
        TypedQuery<Job> query = entityManager.createQuery(criteriaQuery);
        List<Job> result = query.getResultList();
        
        if(jobQP.isIncludeChildJobs()) {
        	List<Job> res = new ArrayList<Job>();
        	
        	for(int i = 0; i < result.size(); i++){
        		Job pJ = result.get(i);
        		res.add(pJ);
        		
        		if(pJ.getParentJobNo() == null) {
        			List<Job> cJList = getChildJobList(pJ.getJobNo(), jobQP);
        			for(int j = 0; j < cJList.size(); j++){
        				if(!res.contains(cJList.get(j))) {
        					res.add(cJList.get(j));
        				}
        			}
        		}
        	}
        	
        	return res;
        }else {
        	return result;
        }
	}
	
	private Job get(long jobNo){
		logger.info("get({})", jobNo);
		return jobRepository.findByJobNo(jobNo);
	}
	
	private Job get(String jobName){
		logger.info("get({})", jobName);
		return jobRepository.findByJobName(jobName);
	}
	
	private Job add(Job job) throws InvalidDataException {
		logger.info("add({})", job);
		
		if(jobExists(job.getJobName())) {
			throw new InvalidDataException(
					ValidationUtils.jobExistsMsg(job.getJobName())
			);
		}
		
		job.setUserCrt(GenUtils.getCurrentUserId());
		job.setDateCrt(LocalDateTime.now());
		 
		Job j = jobRepository.save(job);
		refreshParentJob(job);
		
		return j;
	}
	
	private Job update(long jobNo, Job job) throws InvalidDataException {
		logger.info("update({}, {})", jobNo, job);
		
		if(!jobExists(jobNo)) {
			throw new InvalidDataException(
					ValidationUtils.jobDoesNotExistMsg(jobNo)
			);
		}
		
		job.setUserMod(GenUtils.getCurrentUserId());
		job.setDateMod(LocalDateTime.now());
		
		Job j = jobRepository.save(job);
		refreshParentJob(job);
		
		return j;
	}
	
	private void delete(long jobNo) throws InvalidDataException {
		logger.info("delete({})", jobNo);
		
		if(!jobExists(jobNo)) {
			throw new InvalidDataException(
					ValidationUtils.jobDoesNotExistMsg(jobNo)
			);
		}
		
		if(childJobExists(jobNo)) {
			throw new InvalidDataException(
					ValidationUtils.childJobExistsForJobMsg(jobNo)
			);
		}
		
		if(timesheetExists(jobNo)) {
			throw new InvalidDataException(
					ValidationUtils.timesheetExistsForJobMsg(jobNo)
			);
		}
		
		Job job = get(jobNo);
		jobRepository.delete(job);
		refreshParentJob(job);
	}
	
	private void refreshParentJob(Job job) {
		logger.info("refreshParentJob({})", job);
		
		if(job.getParentJobNo() == null) {
			return;
		}
			
		try {
			jobRepository.refreshParentJob(job.getParentJobNo());
		}catch(Exception e) {
			
		}
	}
	
	private boolean jobExists(long jobNo) {
		Job jobExisting = get(jobNo);
		return (jobExisting != null);
	}
	
	private boolean jobExists(String jobName) {
		Job jobExisting = get(jobName);
		return (jobExisting != null);
	}
	
	private boolean childJobExists(long jobNo) {
		List<Job> jobList = getChildJobList(jobNo);
		return (jobList.size() > 0);
	}
	
	private boolean timesheetExists(long jobNo) {
		return (jobRepository.getTimesheetCount(jobNo) > 0);
	}
	
	//Interface Implementations | Conversion to DTO :---

	@Override
	public List<JobResp> getJobList() {
		return mapper.convertToDto(
				getAll()
		);
	}

	@Override
	public List<JobResp> getJobListByType(String jobType) {
		return mapper.convertToDto(
				getAll(jobType)
		);
	}

	@Override
	public List<JobResp> getJobListByParams(Map<String, String> params) {
		return mapper.convertToDto(
				getAll(mapper.convertToDto(params))
		);
	}

	@Override
	public JobResp getJob(long jobNo) {
		return mapper.convertToDto(
				get(jobNo)
		);
	}

	@Override
	public JobResp addJob(JobReq jobReq) throws InvalidDataException {
		return mapper.convertToDto(
				add(mapper.convertToModel(jobReq))
		);
	}

	@Override
	public JobResp updateJob(long jobNo, JobReq jobReq) throws InvalidDataException {
		return mapper.convertToDto(
				update(jobNo, mapper.convertToModel(jobReq, get(jobNo)))
		);
	}

	@Override
	public DeleteResp deleteJob(long jobNo) throws InvalidDataException {
		delete(jobNo);
		return new DeleteResp(ValidationUtils.jobDeletedMsg(jobNo));
	}
}
