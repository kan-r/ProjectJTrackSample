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

import com.jtrack.dto.JobStatusReq;
import com.jtrack.dto.JobStatusResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.JobStatusMapper;
import com.jtrack.model.JobStatus;
import com.jtrack.model.User;
import com.jtrack.validation.ValidationUtils;

// testing using in memory (H2) database
@DataJpaTest
class JobStatusServiceTest {
	
	@Autowired
	private JobStatusService jobStatusService;
	
	@Autowired
    private TestEntityManager testEntityManager;
	
	@TestConfiguration
    static class JobStatusServiceConfiguration {
		
		@Bean
		public JobStatusService jobStatusService() {
			return new JobStatusServiceImpl();
		}
		
		@Bean 
		public JobStatusMapper jobStatusMapper() {
			return new JobStatusMapper();
		}
    }
	
	@BeforeEach
	void setUp() throws Exception {
		
		testEntityManager.persist(new User("KAN", "kan10", "Kan", "Ranganathan", true, null, "KAN", null, null));
		
		testEntityManager.persist(new JobStatus("In Progress", "Work in progress", true, null, "KAN", null, null));
		testEntityManager.persist(new JobStatus("Completed", "Completed with some resolution", true, null, "KAN", null, null));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobStatusList() {
		assertThat(jobStatusService.getJobStatusList()).hasSize(2);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobStatus() {
		
		String id = "Completed";
		assertThat(jobStatusService.getJobStatus(id).getJobStatus()).isEqualTo(id);
	}
	
	@Test
	void testGetJobStatus_Does_Not_Exist() {
		
		String id = "Unknown";
		assertThat(jobStatusService.getJobStatus(id)).isEqualTo(null);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testAddJobStatus() {
		
		String id = "Test";
		
		JobStatusReq jobStatusReq = new JobStatusReq();
		jobStatusReq.setJobStatus(id);
		jobStatusReq.setJobStatusDesc("Test");
		jobStatusReq.setActive(true);
		
		try {
			assertThat(jobStatusService.addJobStatus(jobStatusReq).getJobStatus()).isEqualTo(id);
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testAddJobStatus_JobStatus_Exists() {
		
		String id = "Completed";
		
		JobStatusReq jobStatusReq = new JobStatusReq();
		jobStatusReq.setJobStatus(id);
		jobStatusReq.setJobStatusDesc("Completed with some resolution");
		jobStatusReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobStatusService.addJobStatus(jobStatusReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobStatusExistsMsg(id));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobStatus() {
		
		String id = "Completed";
		
		JobStatusReq jobStatusReq = new JobStatusReq();
		jobStatusReq.setJobStatusDesc("Testing");
		jobStatusReq.setActive(true);
		
		try {
			JobStatusResp jobStatusResp = jobStatusService.updateJobStatus(id, jobStatusReq);
			
			assertThat(jobStatusResp.getJobStatus()).isEqualTo(id);
			assertThat(jobStatusResp.getJobStatusDesc()).isEqualTo("Testing");
			
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testUpdateJobStatus_JobStatus_Does_Not_Exist() {
		
		String id = "Unknown";
		
		JobStatusReq jobStatusReq = new JobStatusReq();
		jobStatusReq.setJobStatusDesc("Testing");
		jobStatusReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobStatusService.updateJobStatus(id, jobStatusReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobStatusDoesNotExistMsg(id));
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testDeleteJobStatus() {
		
		String id = "Completed";
		
		try {
			assertThat(jobStatusService.deleteJobStatus(id).getMessage()).isEqualTo(ValidationUtils.jobStatusDeletedMsg(id));
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testDeleteJobStatus_JobStatus_Does_Not_Exist() {
		
		String id = "Unknown";
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobStatusService.deleteJobStatus(id);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobStatusDoesNotExistMsg(id));
	}

}
