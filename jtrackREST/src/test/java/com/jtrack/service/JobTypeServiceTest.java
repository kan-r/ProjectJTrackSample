package com.jtrack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.jtrack.dto.JobTypeReq;
import com.jtrack.dto.JobTypeResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.JobTypeMapper;
import com.jtrack.model.JobType;
import com.jtrack.model.User;
import com.jtrack.validation.ValidationUtils;

// testing using in memory (H2) database
@DataJpaTest
class JobTypeServiceTest {
	
	@Autowired
	private JobTypeService jobTypeService;
	
	@Autowired
    private TestEntityManager testEntityManager;
	
	@TestConfiguration
    static class JobTypeServiceConfiguration {
		
		@Bean
		public JobTypeService jobTypeService() {
			return new JobTypeServiceImpl();
		}
		
		@Bean 
		public JobTypeMapper jobTypeMapper() {
			return new JobTypeMapper();
		}
    }
	
	@BeforeEach
	void setUp() throws Exception {
		
		testEntityManager.persist(new User("KAN", "kan10", "Kan", "Ranganathan", true, null, "KAN", null, null));
		
		testEntityManager.persist(new JobType("Project", "Job to group some tasks as a Project", true, null, "KAN", null, null));
		testEntityManager.persist(new JobType("Task", "Task is part of a project", true, null, "KAN", null, null));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobTypeList() {
		assertThat(jobTypeService.getJobTypeList()).hasSize(2);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobType() {
		
		String id = "Task";
		assertThat(jobTypeService.getJobType(id).getJobType()).isEqualTo(id);
	}
	
	@Test
	void testGetJobType_Does_Not_Exist() {
		
		String id = "Unknown";
		assertThat(jobTypeService.getJobType(id)).isEqualTo(null);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testAddJobType() {
		
		String id = "Test";
		
		JobTypeReq jobTypeReq = new JobTypeReq();
		jobTypeReq.setJobType(id);
		jobTypeReq.setJobTypeDesc("Test");
		jobTypeReq.setActive(true);
		
		try {
			assertThat(jobTypeService.addJobType(jobTypeReq).getJobType()).isEqualTo(id);
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testAddJobType_JobType_Exists() {
		
		String id = "Task";
		
		JobTypeReq jobTypeReq = new JobTypeReq();
		jobTypeReq.setJobType(id);
		jobTypeReq.setJobTypeDesc("Task is part of a project");
		jobTypeReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobTypeService.addJobType(jobTypeReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobTypeExistsMsg(id));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobType() {
		
		String id = "Task";
		
		JobTypeReq jobTypeReq = new JobTypeReq();
		jobTypeReq.setJobTypeDesc("Testing");
		jobTypeReq.setActive(true);
		
		try {
			JobTypeResp jobTypeResp = jobTypeService.updateJobType(id, jobTypeReq);
			
			assertThat(jobTypeResp.getJobType()).isEqualTo(id);
			assertThat(jobTypeResp.getJobTypeDesc()).isEqualTo("Testing");
			
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testUpdateJobType_JobType_Does_Not_Exist() {
		
		String id = "Unknown";
		
		JobTypeReq jobTypeReq = new JobTypeReq();
		jobTypeReq.setJobTypeDesc("Testing");
		jobTypeReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobTypeService.updateJobType(id, jobTypeReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobTypeDoesNotExistMsg(id));
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testDeleteJobType() {
		
		String id = "Task";
		
		try {
			assertThat(jobTypeService.deleteJobType(id).getMessage()).isEqualTo(ValidationUtils.jobTypeDeletedMsg(id));
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testDeleteJobType_JobType_Does_Not_Exist() {
		
		String id = "Unknown";
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobTypeService.deleteJobType(id);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobTypeDoesNotExistMsg(id));
	}

}
