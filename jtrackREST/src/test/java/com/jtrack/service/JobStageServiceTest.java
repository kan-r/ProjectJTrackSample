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

import com.jtrack.dto.JobStageReq;
import com.jtrack.dto.JobStageResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.JobStageMapper;
import com.jtrack.model.JobStage;
import com.jtrack.model.User;
import com.jtrack.validation.ValidationUtils;

// testing using in memory (H2) database
@DataJpaTest
class JobStageServiceTest {
	
	@Autowired
	private JobStageService jobStageService;
	
	@Autowired
    private TestEntityManager testEntityManager;
	
	@TestConfiguration
    static class JobStageServiceConfiguration {
		
		@Bean
		public JobStageService jobStageService() {
			return new JobStageServiceImpl();
		}
		
		@Bean 
		public JobStageMapper jobStageMapper() {
			return new JobStageMapper();
		}
    }
	
	@BeforeEach
	void setUp() throws Exception {
		
		testEntityManager.persist(new User("KAN", "kan10", "Kan", "Ranganathan", true, null, "KAN", null, null));
		
		testEntityManager.persist(new JobStage("Design", "Requirements gathering and high level design", true, null, "KAN", null, null));
		testEntityManager.persist(new JobStage("Development", "Detailed level design, coding and unit testing", true, null, "KAN", null, null));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobStageList() {
		assertThat(jobStageService.getJobStageList()).hasSize(2);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobStage() {
		
		String id = "Design";
		assertThat(jobStageService.getJobStage(id).getJobStage()).isEqualTo(id);
	}
	
	@Test
	void testGetJobStage_Does_Not_Exist() {
		
		String id = "Unknown";
		assertThat(jobStageService.getJobStage(id)).isEqualTo(null);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testAddJobStage() {
		
		String id = "Test";
		
		JobStageReq jobStageReq = new JobStageReq();
		jobStageReq.setJobStage(id);
		jobStageReq.setJobStageDesc("Test");
		jobStageReq.setActive(true);
		
		try {
			assertThat(jobStageService.addJobStage(jobStageReq).getJobStage()).isEqualTo(id);
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testAddJobStage_JobStage_Exists() {
		
		String id = "Design";
		
		JobStageReq jobStageReq = new JobStageReq();
		jobStageReq.setJobStage(id);
		jobStageReq.setJobStageDesc("Requirements gathering and high level design");
		jobStageReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobStageService.addJobStage(jobStageReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobStageExistsMsg(id));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobStage() {
		
		String id = "Design";
		
		JobStageReq jobStageReq = new JobStageReq();
		jobStageReq.setJobStageDesc("Testing");
		jobStageReq.setActive(true);
		
		try {
			JobStageResp jobStageResp = jobStageService.updateJobStage(id, jobStageReq);
			
			assertThat(jobStageResp.getJobStage()).isEqualTo(id);
			assertThat(jobStageResp.getJobStageDesc()).isEqualTo("Testing");
			
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testUpdateJobStage_JobStage_Does_Not_Exist() {
		
		String id = "Unknown";
		
		JobStageReq jobStageReq = new JobStageReq();
		jobStageReq.setJobStageDesc("Testing");
		jobStageReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobStageService.updateJobStage(id, jobStageReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobStageDoesNotExistMsg(id));
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testDeleteJobStage() {
		
		String id = "Design";
	
		try {
			assertThat(jobStageService.deleteJobStage(id).getMessage()).isEqualTo(ValidationUtils.jobStageDeletedMsg(id));
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testDeleteJobStage_JobStage_Does_Not_Exist() {
		
		String id = "Unknown";
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobStageService.deleteJobStage(id);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobStageDoesNotExistMsg(id));
	}

}
