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
import com.jtrack.dto.JobStatusReq;
import com.jtrack.dto.JobStatusResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.JobStatusService;
import com.jtrack.validation.ValidationUtils;

@ActiveProfiles("test")
@WebMvcTest(JobStatusController.class)
class JobStatusControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private JobStatusService jobStatusService;
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobStatusList() throws Exception {
		
		List<JobStatusResp> jobStatusList = new ArrayList<JobStatusResp>();
		jobStatusList.add(new JobStatusResp("Completed", "Completed with some resolution", true));
		jobStatusList.add(new JobStatusResp("In Progress", "Work in progress", true));
		
		when(jobStatusService.getJobStatusList()).thenReturn(jobStatusList);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStatuses")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].jobStatus").value("Completed"));
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobStatus() throws Exception {
		
		String id = "Completed";
		JobStatusResp jobStatus = new JobStatusResp(id, "Completed with some resolution", true);
		
		when(jobStatusService.getJobStatus(id)).thenReturn(jobStatus);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStatuses/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobStatus").value(id));
	}
	
	@Test
	void testGetJobStatus_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		
		when(jobStatusService.getJobStatus(id)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStatuses/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testAddJobStatus() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"TesT - 1_2\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		JobStatusReq jobStatusReq2 = new JobStatusReq();
		jobStatusReq2.setJobStatus("TesT - 1_2");
		jobStatusReq2.setJobStatusDesc("Test");
		jobStatusReq2.setActive(true);
		
		JobStatusResp JobStatusResp = new JobStatusResp("TesT - 1_2", "Test", true);
		
		when(jobStatusService.addJobStatus(jobStatusReq2)).thenReturn(JobStatusResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobStatus").value("TesT - 1_2"));
	}
	
	@Test
	void testAddJobStatus_JobStatus_Exists() throws Exception {
		
		String id = "Completed";
		String jobStatusReq = "{\"jobStatus\": \"" + id + "\", \"jobStatusDesc\": \"Completed\", \"active\": true}";
		
		JobStatusReq jobStatusReq2 = new JobStatusReq();
		jobStatusReq2.setJobStatus(id);
		jobStatusReq2.setJobStatusDesc("Completed");
		jobStatusReq2.setActive(true);
		
		when(jobStatusService.addJobStatus(jobStatusReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobStatusExistsMsg(jobStatusReq2.getJobStatus())
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStatusExistsMsg(id)));
	}
	
	@Test
	void testAddJobStatus_JobStatus_Null() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": null, \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_JOB_STATUS_MSG));
	}
	
	@Test
	void testAddJobStatus_JobStatus_Empty() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STATUS_MSG));
	}
	
	@Test
	void testAddJobStatus_JobStatus_Blank() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \" \", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STATUS_MSG));
	}
	
	@Test
	void testAddJobStatus_JobStatus_Start_With_Invalid_Char() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"123\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STATUS_MSG));
	}
	
	@Test
	void testAddJobStatus_JobStatus_End_With_Invalid_Char() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"T1@\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STATUS_MSG));
	}
	
	@Test
	void testAddJobStatus_JobStatus_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"TEST@10\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STATUS_MSG));
	}
	
	@Test
	void testAddJobStatus_JobStatus_With_1_Char() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"T\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STATUS_MSG));
	}
	
	@Test
	void testAddJobStatus_JobStatus_With_2_Chars() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"T1\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		JobStatusReq jobStatusReq2 = new JobStatusReq();
		jobStatusReq2.setJobStatus("T1");
		jobStatusReq2.setJobStatusDesc("Test");
		jobStatusReq2.setActive(true);
		
		JobStatusResp JobStatusResp = new JobStatusResp("T1", "Test", true);
		
		when(jobStatusService.addJobStatus(jobStatusReq2)).thenReturn(JobStatusResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobStatus").value("T1"));
	}
	
	@Test
	void testAddJobStatus_JobStatus_With_20_Chars() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"T123456789T123456789\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		JobStatusReq jobStatusReq2 = new JobStatusReq();
		jobStatusReq2.setJobStatus("T123456789T123456789");
		jobStatusReq2.setJobStatusDesc("Test");
		jobStatusReq2.setActive(true);
		
		JobStatusResp JobStatusResp = new JobStatusResp("T123456789T123456789", "Test", true);
		
		when(jobStatusService.addJobStatus(jobStatusReq2)).thenReturn(JobStatusResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobStatus").value("T123456789T123456789"));
	}
	
	@Test
	void testAddJobStatus_JobStatus_With_21_Chars() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"T123456789T1234567891\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STATUS_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobStatus() throws Exception {
		
		String id = "Completed";
		String jobStatusReq = "{\"jobStatusDesc\": \"Testing\", \"active\": true}";
		
		JobStatusReq jobStatusReq2 = new JobStatusReq();
		jobStatusReq2.setJobStatusDesc("Testing");
		jobStatusReq2.setActive(true);
		
		JobStatusResp JobStatusResp = new JobStatusResp(id, "Testing", true);
		
		when(jobStatusService.updateJobStatus(id, jobStatusReq2)).thenReturn(JobStatusResp);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobStatuses/{id}", id)
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobStatusDesc").value("Testing"));
	}
	
	@Test
	void testUpdateJobStatus_JobStatus_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		String jobStatusReq = "{\"jobStatusDesc\": \"Testing\", \"active\": true}";
		
		JobStatusReq jobStatusReq2 = new JobStatusReq();
		jobStatusReq2.setJobStatusDesc("Testing");
		jobStatusReq2.setActive(true);
		
		when(jobStatusService.updateJobStatus(id, jobStatusReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobStatusDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobStatuses/{id}", id)
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStatusDoesNotExistMsg(id)));
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteJobStatus() throws Exception {
		
		String id = "Completed";
		
		when(jobStatusService.deleteJobStatus(id)).thenReturn(
				new DeleteResp(ValidationUtils.jobStatusDeletedMsg(id))
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobStatuses/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStatusDeletedMsg(id)));
	}
	
	@Test
	void testDeleteJobStatus_JobStatus_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		
		when(jobStatusService.deleteJobStatus(id)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobStatusDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobStatuses/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStatusDoesNotExistMsg(id)));
	}

}
