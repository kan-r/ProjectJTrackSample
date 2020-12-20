package com.jtrack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jtrack.dto.JobPriorityReq;
import com.jtrack.dto.JobPriorityResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.JobPriorityMapper;
import com.jtrack.model.JobPriority;
import com.jtrack.repository.JobPriorityRepository;
import com.jtrack.validation.ValidationUtils;

// testing using MockBeans
@ExtendWith(SpringExtension.class)
class JobPriorityServiceTest2 {
	
	@Autowired
	private JobPriorityService jobPriorityService;
	
	@MockBean
	private JobPriorityRepository jobPriorityRepository;
	
	@TestConfiguration
    static class JobPriorityServiceConfiguration {
		
		@Bean
		public JobPriorityService jobPriorityService() {
			return new JobPriorityServiceImpl();
		}
		
		@Bean 
		public JobPriorityMapper jobPriorityMapper() {
			return new JobPriorityMapper();
		}
 
		@Bean
		public ModelMapper modelMapper() {
			
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			
			return modelMapper;
		}
    }
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobPriorityList() {
		
		List<JobPriority> jobPriorityList = new ArrayList<JobPriority>();
		jobPriorityList.add(new JobPriority("High", "High Priority", true, null, "KAN", null, null));
		jobPriorityList.add(new JobPriority("Low", "Low Priority", true, null, "KAN", null, null));
		
		when(jobPriorityRepository.findAll(Sort.by("jobPriority"))).thenReturn(jobPriorityList);
		assertThat(jobPriorityService.getJobPriorityList()).hasSize(2);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobPriority() {
		
		String id = "High";
		JobPriority jobPriority = new JobPriority(id, "High Priority", true, null, "KAN", null, null);
		
		when(jobPriorityRepository.findById(id)).thenReturn(Optional.of(jobPriority));
		assertThat(jobPriorityService.getJobPriority(id).getJobPriority()).isEqualTo(id);
	}
	
	@Test
	void testGetJobPriority_Does_Not_Exist() {
		
		String id = "Unknown";
		
		when(jobPriorityRepository.findById(id)).thenReturn(Optional.empty());
		assertThat(jobPriorityService.getJobPriority(id)).isEqualTo(null);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testAddJobPriority() {
		
		String id = "Test";
		JobPriority jobPriority = new JobPriority(id, "Test", true, null, "KAN", null, null);
		
		JobPriorityReq jobPriorityReq = new JobPriorityReq();
		jobPriorityReq.setJobPriority(id);
		jobPriorityReq.setJobPriorityDesc("Test");
		jobPriorityReq.setActive(true);
		
		when(jobPriorityRepository.findById(id)).thenReturn(Optional.empty());
		when(jobPriorityRepository.save(jobPriority)).thenReturn(jobPriority);
		
		try {
			assertThat(jobPriorityService.addJobPriority(jobPriorityReq).getJobPriority()).isEqualTo(id);
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testAddJobPriority_JobPriority_Exists() {
		
		String id = "High";
		JobPriority jobPriority = new JobPriority(id, "High Priority", true, null, "KAN", null, null);
		
		JobPriorityReq jobPriorityReq = new JobPriorityReq();
		jobPriorityReq.setJobPriority(id);
		jobPriorityReq.setJobPriorityDesc("High Priority");
		jobPriorityReq.setActive(true);
		
		when(jobPriorityRepository.findById(id)).thenReturn(Optional.of(jobPriority));
//		when(jobPriorityRepository.save(jobPriority)).thenReturn(jobPriority);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobPriorityService.addJobPriority(jobPriorityReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobPriorityExistsMsg(id));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobPriority() {
		
		String id = "High";
		JobPriority jobPriority = new JobPriority(id, "High Priority", true, null, "KAN", null, null);
		
		JobPriorityReq jobPriorityReq = new JobPriorityReq();
		jobPriorityReq.setJobPriorityDesc("Testing");
		jobPriorityReq.setActive(true);
		
		when(jobPriorityRepository.findById(id)).thenReturn(Optional.of(jobPriority));
		when(jobPriorityRepository.save(jobPriority)).thenReturn(jobPriority);
		
		try {
			JobPriorityResp jobPriorityResp = jobPriorityService.updateJobPriority(id, jobPriorityReq);
			
			assertThat(jobPriorityResp.getJobPriority()).isEqualTo(id);
			assertThat(jobPriorityResp.getJobPriorityDesc()).isEqualTo("Testing");
			
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testUpdateJobPriority_JobPriority_Does_Not_Exist() {
		
		String id = "Unknown";
		
		JobPriorityReq jobPriorityReq = new JobPriorityReq();
		jobPriorityReq.setJobPriorityDesc("Testing");
		jobPriorityReq.setActive(true);
		
		when(jobPriorityRepository.findById(id)).thenReturn(Optional.empty());
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobPriorityService.updateJobPriority(id, jobPriorityReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobPriorityDoesNotExistMsg(id));
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testDeleteJobPriority() {
		
		String id = "High";
		JobPriority jobPriority = new JobPriority(id, "High Priority", true, null, "KAN", null, null);
		
		when(jobPriorityRepository.findById(id)).thenReturn(Optional.of(jobPriority));
		doNothing().when(jobPriorityRepository).deleteById(id);
		
		try {
			assertThat(jobPriorityService.deleteJobPriority(id).getMessage()).isEqualTo(ValidationUtils.jobPriorityDeletedMsg(id));
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testDeleteJobPriority_JobPriority_Does_Not_Exist() {
		
		String id = "Unknown";
		
		when(jobPriorityRepository.findById(id)).thenReturn(Optional.empty());
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobPriorityService.deleteJobPriority(id);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobPriorityDoesNotExistMsg(id));
	}

}
