package com.jtrack.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.jtrack.dto.JobReq;
import com.jtrack.dto.JobResp;
import com.jtrack.dto.JobRespKeyDet;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.JobService;
import com.jtrack.validation.ValidationUtils;

@ActiveProfiles("test")
@WebMvcTest(JobController.class)
class JobControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private JobService jobService;
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobList() throws Exception {
		
		List<JobResp> jobList = new ArrayList<JobResp>();
		jobList.add(new JobResp(1, "JTrack", "Job Tracking System", "Project", null, true));
		jobList.add(new JobResp(2, "JTrack - Job Functionality", "Develop JobService & JobController", "Task", new JobRespKeyDet(1, "JTrack", "", "Project"), true));
		jobList.add(new JobResp(3, "JTrack - Bug", "Job Tracking System - Bug", "Adhoc", null, true));
		
		when(jobService.getJobList()).thenReturn(jobList);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(3)))
				.andExpect(jsonPath("$[0].jobName").value("JTrack"));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobListByType() throws Exception {
		
		List<JobResp> jobList = new ArrayList<JobResp>();
		jobList.add(new JobResp(1, "JTrack", "Job Tracking System", "Project", null, true));
		
		when(jobService.getJobListByType("Project")).thenReturn(jobList);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs?type=Project")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(jsonPath("$[0].jobName").value("JTrack"));
		
		when(jobService.getJobListByType("Bug")).thenReturn(new ArrayList<JobResp>());
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs?type=Bug")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(0)));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobListByParams() throws Exception {
		
		String params = "name=JTrack&type=&status=&assignedTo=&includeChild=true&nameC=&typeC=&statusC=&assignedToC=";
		
		Map<String,String> params2 = new HashMap<String, String>();
		params2.put("name", "JTrack");
		params2.put("type", "");
		params2.put("status", "");
		params2.put("assignedTo", "");
		params2.put("includeChild", "true");
		params2.put("nameC", "");
		params2.put("typeC", "");
		params2.put("statusC", "");
		params2.put("assignedToC", "");
		
		List<JobResp> jobList = new ArrayList<JobResp>();
		jobList.add(new JobResp(1, "JTrack", "Job Tracking System", "Project", null, true));
		jobList.add(new JobResp(2, "JTrack - Job Functionality", "Develop JobService & JobController", "Task", new JobRespKeyDet(1, "JTrack", "", "Project"), true));
		
		when(jobService.getJobListByParams(params2)).thenReturn(jobList);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs?" + params)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].jobName").value("JTrack"))
				.andExpect(jsonPath("$[1].jobName").value("JTrack - Job Functionality"));
		
		//-----------------------------------------------------------------------------------
		
		params = "name=JTrack%&type=Adhoc&status=&assignedTo=&includeChild=true&nameC=&typeC=&statusC=&assignedToC=";
		
		params2.put("name", "JTrack%");
		params2.put("type", "Adhoc");
		
		jobList.clear();
		jobList.add(new JobResp(3, "JTrack - Bug", "Job Tracking System - Bug", "Adhoc", null, true));
		
		when(jobService.getJobListByParams(params2)).thenReturn(jobList);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs?" + params)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(jsonPath("$[0].jobName").value("JTrack - Bug"));
		
		//-----------------------------------------------------------------------------------
		
		params = "name=JTrack%&type=Adhoc&status=Scheduled&assignedTo=&includeChild=true&nameC=&typeC=&statusC=&assignedToC=";
		
		params2.put("name", "JTrack%");
		params2.put("type", "Adhoc");
		params2.put("status", "Scheduled");
		
		when(jobService.getJobListByParams(params2)).thenReturn(new ArrayList<JobResp>());
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs?" + params)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(0)));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJob() throws Exception {
		
		long jobNo = 3;
		JobResp job = new JobResp(jobNo, "JTrack - Bug", "Job Tracking System - Bug", "Adhoc", null, true);
	
		when(jobService.getJob(jobNo)).thenReturn(job);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs/{id}", jobNo)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobName").value("JTrack - Bug"));
	}
	
	@Test
	void testGetJob_Does_Not_Exist() throws Exception {
		
		long jobNo = 3;
		
		when(jobService.getJob(jobNo)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs/{id}", jobNo)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testAddJob() throws Exception {
		
		String jobReq = "{\"jobName\": \"TesT - 1_2\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		JobReq jobReq2 = new JobReq();
		jobReq2.setJobName("TesT - 1_2");
		jobReq2.setJobType("Adhoc");
		jobReq2.setActive(true);
		
		JobResp job = new JobResp(1, "TesT - 1_2", "", "Adhoc", null, true);
	
		when(jobService.addJob(jobReq2)).thenReturn(job);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobName").value("TesT - 1_2"));
	}
	
	@Test
	void testAddJob_Job_Exists() throws Exception {
		
		String jobReq = "{\"jobName\": \"JTrack\", \"jobType\": \"Project\", \"active\": true}";
		
		JobReq jobReq2 = new JobReq();
		jobReq2.setJobName("JTrack");
		jobReq2.setJobType("Project");
		jobReq2.setActive(true);
		
		when(jobService.addJob(jobReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobExistsMsg(jobReq2.getJobName())
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobExistsMsg("JTrack")));
	}
	
	@Test
	void testAddJob_Job_Null() throws Exception {
		
		String jobReq = "{\"jobName\": null, \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_JOB_MSG));
	}
	
	@Test
	void testAddJob_Job_Empty() throws Exception {
		
		String jobReq = "{\"jobName\": \"\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_MSG));
	}
	
	@Test
	void testAddJob_Job_Blank() throws Exception {
		
		String jobReq = "{\"jobName\": \" \", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_MSG));
	}
	
	@Test
	void testAddJob_Job_Start_With_Invalid_Char() throws Exception {
		
		String jobReq = "{\"jobName\": \"123\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_MSG));
	}
	
	@Test
	void testAddJob_Job_End_With_Invalid_Char() throws Exception {
		
		String jobReq = "{\"jobName\": \"T1@\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_MSG));
	}
	
	@Test
	void testAddJob_Job_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String jobReq = "{\"jobName\": \"Test@10\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_MSG));
	}
	
	@Test
	void testAddJob_Job_With_1_Char() throws Exception {
		
		String jobReq = "{\"jobName\": \"T\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_MSG));
	}
	
	@Test
	void testAddJob_Job_With_2_Chars() throws Exception {
		
		String jobReq = "{\"jobName\": \"T1\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		JobReq jobReq2 = new JobReq();
		jobReq2.setJobName("T1");
		jobReq2.setJobType("Adhoc");
		jobReq2.setActive(true);
		
		JobResp job = new JobResp(1, "T1", "", "Adhoc", null, true);
		
		when(jobService.addJob(jobReq2)).thenReturn(job);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobName").value("T1"));
	}
	
	@Test
	void testAddJob_Job_With_20_Chars() throws Exception {
		
		String jobReq = "{\"jobName\": \"T123456789T123456789\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		JobReq jobReq2 = new JobReq();
		jobReq2.setJobName("T123456789T123456789");
		jobReq2.setJobType("Adhoc");
		jobReq2.setActive(true);
		
		JobResp job = new JobResp(1, "T123456789T123456789", "", "Adhoc", null, true);
		
		when(jobService.addJob(jobReq2)).thenReturn(job);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobName").value("T123456789T123456789"));
	}
	
	@Test
	void testAddJob_Job_With_21_Chars() throws Exception {
		
		String jobReq = "{\"jobName\": \"T123456789T1234567891\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJob() throws Exception {
		
		long jobNo = 1;
		String jobReq = "{\"jobName\": \"Test\", \"jobType\": \"Project\", \"active\": true}";
		
		JobReq jobReq2 = new JobReq();
		jobReq2.setJobName("Test");
		jobReq2.setJobType("Project");
		jobReq2.setActive(true);
		
		JobResp job = new JobResp(jobNo, "Test", "", "Project", null, true);
		
		when(jobService.updateJob(jobNo, jobReq2)).thenReturn(job);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobs/{id}", jobNo)
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobName").value("Test"));
	}
	
	@Test
	void testUpdateJob_Job_Does_Not_Exist() throws Exception {
		
		long jobNo = 0;
		String jobReq = "{\"jobName\": \"Test\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		JobReq jobReq2 = new JobReq();
		jobReq2.setJobName("Test");
		jobReq2.setJobType("Adhoc");
		jobReq2.setActive(true);
		
		when(jobService.updateJob(jobNo, jobReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobDoesNotExistMsg(jobNo)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobs/{id}", jobNo)
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobDoesNotExistMsg(jobNo)));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteJob() throws Exception {
		
		long jobNo = 1;
		
		when(jobService.deleteJob(jobNo)).thenReturn(
				new DeleteResp(ValidationUtils.jobDeletedMsg(jobNo))
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobs/{id}", jobNo)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobDeletedMsg(jobNo)));
	}
	
	@Test
	void testDeleteJob_Job_Does_Not_Exist() throws Exception {
		
		long jobNo = 0;
		
		when(jobService.deleteJob(jobNo)).thenThrow(
				new InvalidDataException(
						ValidationUtils.jobDoesNotExistMsg(jobNo)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobs/{id}", jobNo)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobDoesNotExistMsg(jobNo)));
	}

}
