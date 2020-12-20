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
import com.jtrack.dto.JobTypeReq;
import com.jtrack.dto.JobTypeResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.JobTypeService;
import com.jtrack.validation.ValidationUtils;

@ActiveProfiles("test")
@WebMvcTest(JobTypeController.class)
class JobTypeControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private JobTypeService jobTypeService;
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobTypeList() throws Exception {
		
		List<JobTypeResp> jobTypeList = new ArrayList<JobTypeResp>();
		jobTypeList.add(new JobTypeResp("Project", "Job to group some tasks as a Project", true));
		jobTypeList.add(new JobTypeResp("Task", "Task is part of a project", true));
		
		when(jobTypeService.getJobTypeList()).thenReturn(jobTypeList);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobTypes")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].jobType").value("Project"));
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobType() throws Exception {
		
		String id = "Task";
		JobTypeResp jobType = new JobTypeResp(id, "Task is part of a project", true);
		
		when(jobTypeService.getJobType(id)).thenReturn(jobType);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobTypes/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobType").value(id));
	}
	
	@Test
	void testGetJobType_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		
		when(jobTypeService.getJobType(id)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobTypes/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testAddJobType() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"TesT - 1_2\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		JobTypeReq jobTypeReq2 = new JobTypeReq();
		jobTypeReq2.setJobType("TesT - 1_2");
		jobTypeReq2.setJobTypeDesc("Test");
		jobTypeReq2.setActive(true);
		
		JobTypeResp JobTypeResp = new JobTypeResp("TesT - 1_2", "Test", true);
		
		when(jobTypeService.addJobType(jobTypeReq2)).thenReturn(JobTypeResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobType").value("TesT - 1_2"));
	}
	
	@Test
	void testAddJobType_JobType_Exists() throws Exception {
		
		String id = "Task";
		String jobTypeReq = "{\"jobType\": \"" + id + "\", \"jobTypeDesc\": \"Task\", \"active\": true}";
		
		JobTypeReq jobTypeReq2 = new JobTypeReq();
		jobTypeReq2.setJobType(id);
		jobTypeReq2.setJobTypeDesc("Task");
		jobTypeReq2.setActive(true);
		
		when(jobTypeService.addJobType(jobTypeReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobTypeExistsMsg(jobTypeReq2.getJobType())
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobTypeExistsMsg(id)));
	}
	
	@Test
	void testAddJobType_JobType_Null() throws Exception {
		
		String jobTypeReq = "{\"jobType\": null, \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_JOB_TYPE_MSG));
	}
	
	@Test
	void testAddJobType_JobType_Empty() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_TYPE_MSG));
	}
	
	@Test
	void testAddJobType_JobType_Blank() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \" \", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_TYPE_MSG));
	}
	
	@Test
	void testAddJobType_JobType_Start_With_Invalid_Char() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"123\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_TYPE_MSG));
	}
	
	@Test
	void testAddJobType_JobType_End_With_Invalid_Char() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"T1@\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_TYPE_MSG));
	}
	
	@Test
	void testAddJobType_JobType_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"TEST@10\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_TYPE_MSG));
	}
	
	@Test
	void testAddJobType_JobType_With_1_Char() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"T\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_TYPE_MSG));
	}
	
	@Test
	void testAddJobType_JobType_With_2_Chars() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"T1\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		JobTypeReq jobTypeReq2 = new JobTypeReq();
		jobTypeReq2.setJobType("T1");
		jobTypeReq2.setJobTypeDesc("Test");
		jobTypeReq2.setActive(true);
		
		JobTypeResp JobTypeResp = new JobTypeResp("T1", "Test", true);
		
		when(jobTypeService.addJobType(jobTypeReq2)).thenReturn(JobTypeResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobType").value("T1"));
	}
	
	@Test
	void testAddJobType_JobType_With_20_Chars() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"T123456789T123456789\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		JobTypeReq jobTypeReq2 = new JobTypeReq();
		jobTypeReq2.setJobType("T123456789T123456789");
		jobTypeReq2.setJobTypeDesc("Test");
		jobTypeReq2.setActive(true);
		
		JobTypeResp JobTypeResp = new JobTypeResp("T123456789T123456789", "Test", true);
		
		when(jobTypeService.addJobType(jobTypeReq2)).thenReturn(JobTypeResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobType").value("T123456789T123456789"));
	}
	
	@Test
	void testAddJobType_JobType_With_21_Chars() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"T123456789T1234567891\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_TYPE_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobType() throws Exception {
		
		String id = "Task";
		String jobTypeReq = "{\"jobTypeDesc\": \"Testing\", \"active\": true}";
		
		JobTypeReq jobTypeReq2 = new JobTypeReq();
		jobTypeReq2.setJobTypeDesc("Testing");
		jobTypeReq2.setActive(true);
		
		JobTypeResp JobTypeResp = new JobTypeResp(id, "Testing", true);
		
		when(jobTypeService.updateJobType(id, jobTypeReq2)).thenReturn(JobTypeResp);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobTypes/{id}", id)
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobTypeDesc").value("Testing"));
	}
	
	@Test
	void testUpdateJobType_JobType_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		String jobTypeReq = "{\"jobTypeDesc\": \"Testing\", \"active\": true}";
		
		JobTypeReq jobTypeReq2 = new JobTypeReq();
		jobTypeReq2.setJobTypeDesc("Testing");
		jobTypeReq2.setActive(true);
		
		when(jobTypeService.updateJobType(id, jobTypeReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobTypeDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobTypes/{id}", id)
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobTypeDoesNotExistMsg(id)));
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteJobType() throws Exception {
		
		String id = "Task";
		
		when(jobTypeService.deleteJobType(id)).thenReturn(
				new DeleteResp(ValidationUtils.jobTypeDeletedMsg(id))
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobTypes/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobTypeDeletedMsg(id)));
	}
	
	@Test
	void testDeleteJobType_JobType_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		
		when(jobTypeService.deleteJobType(id)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobTypeDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobTypes/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobTypeDoesNotExistMsg(id)));
	}

}
