package com.jtrack.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.JobPriorityReq;
import com.jtrack.dto.JobPriorityResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.JobPriorityService;
import com.jtrack.validation.ValidationUtils;

@ActiveProfiles("test")
@WebMvcTest(JobPriorityController.class)
class JobPriorityControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private JobPriorityService jobPriorityService;
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobPriorityList() throws Exception {
		
		List<JobPriorityResp> jobPriorityList = new ArrayList<JobPriorityResp>();
		jobPriorityList.add(new JobPriorityResp("High", "High Priority", true));
		jobPriorityList.add(new JobPriorityResp("Low", "Low Priority", true));
		
		when(jobPriorityService.getJobPriorityList()).thenReturn(jobPriorityList);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobPriorities")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].jobPriority").value("High"));
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobPriority() throws Exception {
		
		String id = "High";
		JobPriorityResp jobPriority = new JobPriorityResp(id, "High Priority", true);
		
		when(jobPriorityService.getJobPriority(id)).thenReturn(jobPriority);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobPriorities/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobPriority").value(id));
	}
	
	@Test
	void testGetJobPriority_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		
		when(jobPriorityService.getJobPriority(id)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobPriorities/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testAddJobPriority() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"TesT - 1_2\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		JobPriorityReq jobPriorityReq2 = new JobPriorityReq();
		jobPriorityReq2.setJobPriority("TesT - 1_2");
		jobPriorityReq2.setJobPriorityDesc("Test");
		jobPriorityReq2.setActive(true);
		
		JobPriorityResp JobPriorityResp = new JobPriorityResp("TesT - 1_2", "Test", true);
		
		when(jobPriorityService.addJobPriority(jobPriorityReq2)).thenReturn(JobPriorityResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobPriority").value("TesT - 1_2"));
	}
	
	@Test
	void testAddJobPriority_JobPriority_Exists() throws Exception {
		
		String id = "High";
		String jobPriorityReq = "{\"jobPriority\": \"" + id + "\", \"jobPriorityDesc\": \"High\", \"active\": true}";
		
		JobPriorityReq jobPriorityReq2 = new JobPriorityReq();
		jobPriorityReq2.setJobPriority(id);
		jobPriorityReq2.setJobPriorityDesc("High");
		jobPriorityReq2.setActive(true);
		
		when(jobPriorityService.addJobPriority(jobPriorityReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobPriorityExistsMsg(jobPriorityReq2.getJobPriority())
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobPriorityExistsMsg(id)));
	}
	
	@Test
	void testAddJobPriority_JobPriority_Null() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": null, \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_JOB_PRIORITY_MSG));
	}
	
	@Test
	void testAddJobPriority_JobPriority_Empty() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_PRIORITY_MSG));
	}
	
	@Test
	void testAddJobPriority_JobPriority_Blank() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \" \", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_PRIORITY_MSG));
	}
	
	@Test
	void testAddJobPriority_JobPriority_Start_With_Invalid_Char() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"123\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_PRIORITY_MSG));
	}
	
	@Test
	void testAddJobPriority_JobPriority_End_With_Invalid_Char() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"T1@\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_PRIORITY_MSG));
	}
	
	@Test
	void testAddJobPriority_JobPriority_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"TEST@10\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_PRIORITY_MSG));
	}
	
	@Test
	void testAddJobPriority_JobPriority_With_1_Char() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"T\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_PRIORITY_MSG));
	}
	
	@Test
	void testAddJobPriority_JobPriority_With_2_Chars() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"T1\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		JobPriorityReq jobPriorityReq2 = new JobPriorityReq();
		jobPriorityReq2.setJobPriority("T1");
		jobPriorityReq2.setJobPriorityDesc("Test");
		jobPriorityReq2.setActive(true);
		
		JobPriorityResp JobPriorityResp = new JobPriorityResp("T1", "Test", true);
		
		when(jobPriorityService.addJobPriority(jobPriorityReq2)).thenReturn(JobPriorityResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobPriority").value("T1"));
	}
	
	@Test
	void testAddJobPriority_JobPriority_With_20_Chars() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"T123456789T123456789\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		JobPriorityReq jobPriorityReq2 = new JobPriorityReq();
		jobPriorityReq2.setJobPriority("T123456789T123456789");
		jobPriorityReq2.setJobPriorityDesc("Test");
		jobPriorityReq2.setActive(true);
		
		JobPriorityResp JobPriorityResp = new JobPriorityResp("T123456789T123456789", "Test", true);
		
		when(jobPriorityService.addJobPriority(jobPriorityReq2)).thenReturn(JobPriorityResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobPriority").value("T123456789T123456789"));
	}
	
	@Test
	void testAddJobPriority_JobPriority_With_21_Chars() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"T123456789T1234567891\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_PRIORITY_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobPriority() throws Exception {
		
		String id = "High";
		String jobPriorityReq = "{\"jobPriorityDesc\": \"Testing\", \"active\": true}";
		
		JobPriorityReq jobPriorityReq2 = new JobPriorityReq();
		jobPriorityReq2.setJobPriorityDesc("Testing");
		jobPriorityReq2.setActive(true);
		
		JobPriorityResp JobPriorityResp = new JobPriorityResp(id, "Testing", true);
		
		when(jobPriorityService.updateJobPriority(id, jobPriorityReq2)).thenReturn(JobPriorityResp);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobPriorities/{id}", id)
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobPriorityDesc").value("Testing"));
	}
	
	@Test
	void testUpdateJobPriority_JobPriority_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		String jobPriorityReq = "{\"jobPriorityDesc\": \"Testing\", \"active\": true}";
		
		JobPriorityReq jobPriorityReq2 = new JobPriorityReq();
		jobPriorityReq2.setJobPriorityDesc("Testing");
		jobPriorityReq2.setActive(true);
		
		when(jobPriorityService.updateJobPriority(id, jobPriorityReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobPriorityDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobPriorities/{id}", id)
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobPriorityDoesNotExistMsg(id)));
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteJobPriority() throws Exception {
		
		String id = "High";
		
		when(jobPriorityService.deleteJobPriority(id)).thenReturn(
				new DeleteResp(ValidationUtils.jobPriorityDeletedMsg(id))
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobPriorities/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobPriorityDeletedMsg(id)));
	}
	
	@Test
	void testDeleteJobPriority_JobPriority_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		
		when(jobPriorityService.deleteJobPriority(id)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobPriorityDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobPriorities/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobPriorityDoesNotExistMsg(id)));
	}

}
