package com.jtrack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.jtrack.dto.JobReq;
import com.jtrack.dto.JobResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.JobMapper;
import com.jtrack.model.Job;
import com.jtrack.model.JobType;
import com.jtrack.model.User;
import com.jtrack.validation.ValidationUtils;

// testing using in memory (H2) database
@DataJpaTest
class JobServiceTest {
	
	@Autowired
	private JobService jobService;
	
	@Autowired
    private TestEntityManager testEntityManager;
	
	private long jobNo;
	
	@TestConfiguration
    static class JobServiceConfiguration {
		
		@Bean
		public JobService jobService() {
			return new JobServiceImpl();
		}
		
		@Bean 
		public JobMapper jobMapper() {
			return new JobMapper();
		}
    }
	
	@BeforeEach
	void setUp() throws Exception {
		
		testEntityManager.persist(new User("KAN", "kan10", "Kan", "Ranganathan", true, null, "KAN", null, null));
		
		testEntityManager.persist(new JobType("Project", "Job to group some tasks as a Project", true, null, "KAN", null, null));
		testEntityManager.persist(new JobType("Task", "Task is part of a project", true, null, "KAN", null, null));
		testEntityManager.persist(new JobType("Adhoc", "Ad hoc job", true, null, "KAN", null, null));
		
		Job job = testEntityManager.persist(new Job("JTrack", "Job Tracking System", "Project", null, true, null, "KAN"));
		testEntityManager.persist(new Job("JTrack - Job Functionality", "Develop JobService & JobController", "Task", job.getJobNo(), true, null, "KAN"));
		job = testEntityManager.persist(new Job("JTrack - Bug", "Job Tracking System - Bug", "Adhoc", null, true, null, "KAN"));
		
		jobNo = job.getJobNo();
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobList() {
		assertThat(jobService.getJobList()).hasSize(3);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobListByType() {
		assertThat(jobService.getJobListByType("Project")).hasSize(1);
		assertThat(jobService.getJobListByType("Bug")).hasSize(0);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobListByParams() {
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("name", "JTrack");
		params.put("type", "");
		params.put("status", "");
		params.put("assignedTo", "");
		params.put("includeChild", "true");
		params.put("nameC", "");
		params.put("typeC", "");
		params.put("statusC", "");
		params.put("assignedToC", "");
		
		assertThat(jobService.getJobListByParams(params)).hasSize(2);
		
		//-----------------------------------------------------------------------------------
		
		params.put("name", "JTrack%");
		params.put("type", "Adhoc");
		
		assertThat(jobService.getJobListByParams(params)).hasSize(1);
		
		//-----------------------------------------------------------------------------------
		
		params.put("name", "JTrack%");
		params.put("type", "Adhoc");
		params.put("status", "Scheduled");
		
		assertThat(jobService.getJobListByParams(params)).hasSize(0);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJob() {
		assertThat(jobService.getJob(jobNo).getJobName()).isEqualTo("JTrack - Bug");
	}
	
	@Test
	void testGetJob_Does_Not_Exist() {
		assertThat(jobService.getJob(0)).isEqualTo(null);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testAddJob() {
		
		JobReq jobReq = new JobReq();
		jobReq.setJobName("TesT - 1_2");
		jobReq.setJobType("Adhoc");
		jobReq.setActive(true);
		
		try {
			assertThat(jobService.addJob(jobReq).getJobName()).isEqualTo("TesT - 1_2");
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testAddJob_Job_Exists() {
		
		JobReq jobReq = new JobReq();
		jobReq.setJobName("JTrack");
		jobReq.setJobType("Project");
		jobReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobService.addJob(jobReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobExistsMsg("JTrack"));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJob() {
		
		JobReq jobReq = new JobReq();
		jobReq.setJobName("Test");
		jobReq.setJobType("Project");
		jobReq.setActive(true);
		
		try {
			JobResp jobResp = jobService.updateJob(jobNo, jobReq);
			
			assertThat(jobResp.getJobNo()).isEqualTo(jobNo);
			assertThat(jobResp.getJobName()).isEqualTo("Test");
			
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testUpdateJob_Job_Does_Not_Exist() {
		
		JobReq jobReq = new JobReq();
		jobReq.setJobName("Test");
		jobReq.setJobType("Adhoc");
		jobReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobService.updateJob(0, jobReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobDoesNotExistMsg(0));
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testDeleteJob() {
	
		try {
			assertThat(jobService.deleteJob(jobNo).getMessage()).isEqualTo(ValidationUtils.jobDeletedMsg(jobNo));
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testDeleteJob_Job_Does_Not_Exist() {
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobService.deleteJob(0);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobDoesNotExistMsg(0));
	}

}
