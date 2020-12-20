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
import com.jtrack.dto.JobResolutionReq;
import com.jtrack.dto.JobResolutionResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.JobResolutionService;
import com.jtrack.validation.ValidationUtils;

@ActiveProfiles("test")
@WebMvcTest(JobResolutionController.class)
class JobResolutionControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private JobResolutionService jobResolutionService;
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobResolutionList() throws Exception {
		
		List<JobResolutionResp> jobResolutionList = new ArrayList<JobResolutionResp>();
		jobResolutionList.add(new JobResolutionResp("Done", "Completed or fixed", true));
		jobResolutionList.add(new JobResolutionResp("Not Replicated", "Issue cannot be replicated", true));
		
		when(jobResolutionService.getJobResolutionList()).thenReturn(jobResolutionList);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobResolutions")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].jobResolution").value("Done"));
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobResolution() throws Exception {
		
		String id = "Done";
		JobResolutionResp jobResolution = new JobResolutionResp(id, "Completed or fixed", true);
		
		when(jobResolutionService.getJobResolution(id)).thenReturn(jobResolution);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobResolutions/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobResolution").value(id));
	}
	
	@Test
	void testGetJobResolution_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		
		when(jobResolutionService.getJobResolution(id)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobResolutions/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testAddJobResolution() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"TesT - 1_2\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		JobResolutionReq jobResolutionReq2 = new JobResolutionReq();
		jobResolutionReq2.setJobResolution("TesT - 1_2");
		jobResolutionReq2.setJobResolutionDesc("Test");
		jobResolutionReq2.setActive(true);
		
		JobResolutionResp JobResolutionResp = new JobResolutionResp("TesT - 1_2", "Test", true);
		
		when(jobResolutionService.addJobResolution(jobResolutionReq2)).thenReturn(JobResolutionResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobResolution").value("TesT - 1_2"));
	}
	
	@Test
	void testAddJobResolution_JobResolution_Exists() throws Exception {
		
		String id = "Done";
		String jobResolutionReq = "{\"jobResolution\": \"" + id + "\", \"jobResolutionDesc\": \"Completed or fixed\", \"active\": true}";
		
		JobResolutionReq jobResolutionReq2 = new JobResolutionReq();
		jobResolutionReq2.setJobResolution(id);
		jobResolutionReq2.setJobResolutionDesc("Completed or fixed");
		jobResolutionReq2.setActive(true);
		
		when(jobResolutionService.addJobResolution(jobResolutionReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobResolutionExistsMsg(jobResolutionReq2.getJobResolution())
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobResolutionExistsMsg(id)));
	}
	
	@Test
	void testAddJobResolution_JobResolution_Null() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": null, \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_JOB_RESOLUTION_MSG));
	}
	
	@Test
	void testAddJobResolution_JobResolution_Empty() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_RESOLUTION_MSG));
	}
	
	@Test
	void testAddJobResolution_JobResolution_Blank() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \" \", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_RESOLUTION_MSG));
	}
	
	@Test
	void testAddJobResolution_JobResolution_Start_With_Invalid_Char() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"123\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_RESOLUTION_MSG));
	}
	
	@Test
	void testAddJobResolution_JobResolution_End_With_Invalid_Char() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"T1@\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_RESOLUTION_MSG));
	}
	
	@Test
	void testAddJobResolution_JobResolution_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"TEST@10\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_RESOLUTION_MSG));
	}
	
	@Test
	void testAddJobResolution_JobResolution_With_1_Char() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"T\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_RESOLUTION_MSG));
	}
	
	@Test
	void testAddJobResolution_JobResolution_With_2_Chars() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"T1\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		JobResolutionReq jobResolutionReq2 = new JobResolutionReq();
		jobResolutionReq2.setJobResolution("T1");
		jobResolutionReq2.setJobResolutionDesc("Test");
		jobResolutionReq2.setActive(true);
		
		JobResolutionResp JobResolutionResp = new JobResolutionResp("T1", "Test", true);
		
		when(jobResolutionService.addJobResolution(jobResolutionReq2)).thenReturn(JobResolutionResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobResolution").value("T1"));
	}
	
	@Test
	void testAddJobResolution_JobResolution_With_20_Chars() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"T123456789T123456789\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		JobResolutionReq jobResolutionReq2 = new JobResolutionReq();
		jobResolutionReq2.setJobResolution("T123456789T123456789");
		jobResolutionReq2.setJobResolutionDesc("Test");
		jobResolutionReq2.setActive(true);
		
		JobResolutionResp JobResolutionResp = new JobResolutionResp("T123456789T123456789", "Test", true);
		
		when(jobResolutionService.addJobResolution(jobResolutionReq2)).thenReturn(JobResolutionResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobResolution").value("T123456789T123456789"));
	}
	
	@Test
	void testAddJobResolution_JobResolution_With_21_Chars() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"T123456789T1234567891\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_RESOLUTION_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobResolution() throws Exception {
		
		String id = "Done";
		String jobResolutionReq = "{\"jobResolutionDesc\": \"Testing\", \"active\": true}";
		
		JobResolutionReq jobResolutionReq2 = new JobResolutionReq();
		jobResolutionReq2.setJobResolutionDesc("Testing");
		jobResolutionReq2.setActive(true);
		
		JobResolutionResp JobResolutionResp = new JobResolutionResp(id, "Testing", true);
		
		when(jobResolutionService.updateJobResolution(id, jobResolutionReq2)).thenReturn(JobResolutionResp);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobResolutions/{id}", id)
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobResolutionDesc").value("Testing"));
	}
	
	@Test
	void testUpdateJobResolution_JobResolution_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		String jobResolutionReq = "{\"jobResolutionDesc\": \"Testing\", \"active\": true}";
		
		JobResolutionReq jobResolutionReq2 = new JobResolutionReq();
		jobResolutionReq2.setJobResolutionDesc("Testing");
		jobResolutionReq2.setActive(true);
		
		when(jobResolutionService.updateJobResolution(id, jobResolutionReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobResolutionDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobResolutions/{id}", id)
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobResolutionDoesNotExistMsg(id)));
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteJobResolution() throws Exception {
		
		String id = "Done";
		
		when(jobResolutionService.deleteJobResolution(id)).thenReturn(
				new DeleteResp(ValidationUtils.jobResolutionDeletedMsg(id))
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobResolutions/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobResolutionDeletedMsg(id)));
	}
	
	@Test
	void testDeleteJobResolution_JobResolution_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		
		when(jobResolutionService.deleteJobResolution(id)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobResolutionDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobResolutions/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobResolutionDoesNotExistMsg(id)));
	}

}
