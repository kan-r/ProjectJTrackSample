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
import com.jtrack.dto.JobStageReq;
import com.jtrack.dto.JobStageResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.JobStageService;
import com.jtrack.validation.ValidationUtils;

@ActiveProfiles("test")
@WebMvcTest(JobStageController.class)
class JobStageControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private JobStageService jobStageService;
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobStageList() throws Exception {
		
		List<JobStageResp> jobStageList = new ArrayList<JobStageResp>();
		jobStageList.add(new JobStageResp("Design", "Requirements gathering and high level design", true));
		jobStageList.add(new JobStageResp("Development", "Detailed level design, coding and unit testing", true));
		
		when(jobStageService.getJobStageList()).thenReturn(jobStageList);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStages")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].jobStage").value("Design"));
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobStage() throws Exception {
		
		String id = "Design";
		JobStageResp jobStage = new JobStageResp(id, "Requirements gathering and high level design", true);
		
		when(jobStageService.getJobStage(id)).thenReturn(jobStage);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStages/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobStage").value(id));
	}
	
	@Test
	void testGetJobStage_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		
		when(jobStageService.getJobStage(id)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStages/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testAddJobStage() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"TesT - 1_2\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		JobStageReq jobStageReq2 = new JobStageReq();
		jobStageReq2.setJobStage("TesT - 1_2");
		jobStageReq2.setJobStageDesc("Test");
		jobStageReq2.setActive(true);
		
		JobStageResp JobStageResp = new JobStageResp("TesT - 1_2", "Test", true);
		
		when(jobStageService.addJobStage(jobStageReq2)).thenReturn(JobStageResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobStage").value("TesT - 1_2"));
	}
	
	@Test
	void testAddJobStage_JobStage_Exists() throws Exception {
		
		String id = "Design";
		String jobStageReq = "{\"jobStage\": \"" + id + "\", \"jobStageDesc\": \"Design\", \"active\": true}";
		
		JobStageReq jobStageReq2 = new JobStageReq();
		jobStageReq2.setJobStage(id);
		jobStageReq2.setJobStageDesc("Design");
		jobStageReq2.setActive(true);
		
		when(jobStageService.addJobStage(jobStageReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobStageExistsMsg(jobStageReq2.getJobStage())
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStageExistsMsg(id)));
	}
	
	@Test
	void testAddJobStage_JobStage_Null() throws Exception {
		
		String jobStageReq = "{\"jobStage\": null, \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_JOB_STAGE_MSG));
	}
	
	@Test
	void testAddJobStage_JobStage_Empty() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STAGE_MSG));
	}
	
	@Test
	void testAddJobStage_JobStage_Blank() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \" \", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STAGE_MSG));
	}
	
	@Test
	void testAddJobStage_JobStage_Start_With_Invalid_Char() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"123\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STAGE_MSG));
	}
	
	@Test
	void testAddJobStage_JobStage_End_With_Invalid_Char() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"T1@\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STAGE_MSG));
	}
	
	@Test
	void testAddJobStage_JobStage_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"TEST@10\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STAGE_MSG));
	}
	
	@Test
	void testAddJobStage_JobStage_With_1_Char() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"T\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STAGE_MSG));
	}
	
	@Test
	void testAddJobStage_JobStage_With_2_Chars() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"T1\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		JobStageReq jobStageReq2 = new JobStageReq();
		jobStageReq2.setJobStage("T1");
		jobStageReq2.setJobStageDesc("Test");
		jobStageReq2.setActive(true);
		
		JobStageResp JobStageResp = new JobStageResp("T1", "Test", true);
		
		when(jobStageService.addJobStage(jobStageReq2)).thenReturn(JobStageResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobStage").value("T1"));
	}
	
	@Test
	void testAddJobStage_JobStage_With_20_Chars() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"T123456789T123456789\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		JobStageReq jobStageReq2 = new JobStageReq();
		jobStageReq2.setJobStage("T123456789T123456789");
		jobStageReq2.setJobStageDesc("Test");
		jobStageReq2.setActive(true);
		
		JobStageResp JobStageResp = new JobStageResp("T123456789T123456789", "Test", true);
		
		when(jobStageService.addJobStage(jobStageReq2)).thenReturn(JobStageResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobStage").value("T123456789T123456789"));
	}
	
	@Test
	void testAddJobStage_JobStage_With_21_Chars() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"T123456789T1234567891\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STAGE_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobStage() throws Exception {
		
		String id = "Design";
		String jobStageReq = "{\"jobStageDesc\": \"Testing\", \"active\": true}";
		
		JobStageReq jobStageReq2 = new JobStageReq();
		jobStageReq2.setJobStageDesc("Testing");
		jobStageReq2.setActive(true);
		
		JobStageResp JobStageResp = new JobStageResp(id, "Testing", true);
		
		when(jobStageService.updateJobStage(id, jobStageReq2)).thenReturn(JobStageResp);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobStages/{id}", id)
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobStageDesc").value("Testing"));
	}
	
	@Test
	void testUpdateJobStage_JobStage_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		String jobStageReq = "{\"jobStageDesc\": \"Testing\", \"active\": true}";
		
		JobStageReq jobStageReq2 = new JobStageReq();
		jobStageReq2.setJobStageDesc("Testing");
		jobStageReq2.setActive(true);
		
		when(jobStageService.updateJobStage(id, jobStageReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobStageDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobStages/{id}", id)
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStageDoesNotExistMsg(id)));
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteJobStage() throws Exception {
		
		String id = "Design";
		
		when(jobStageService.deleteJobStage(id)).thenReturn(
				new DeleteResp(ValidationUtils.jobStageDeletedMsg(id))
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobStages/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStageDeletedMsg(id)));
	}
	
	@Test
	void testDeleteJobStage_JobStage_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		
		when(jobStageService.deleteJobStage(id)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobStageDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobStages/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStageDoesNotExistMsg(id)));
	}

}
