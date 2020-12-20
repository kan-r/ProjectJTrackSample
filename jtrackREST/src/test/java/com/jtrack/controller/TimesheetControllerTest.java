package com.jtrack.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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
import com.jtrack.dto.TimesheetReq;
import com.jtrack.dto.TimesheetReqUpd;
import com.jtrack.dto.TimesheetResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.TimesheetService;
import com.jtrack.util.GenUtils;
import com.jtrack.validation.ValidationUtils;

@ActiveProfiles("test")
@WebMvcTest(TimesheetController.class)
class TimesheetControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private TimesheetService timesheetService;
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetTimesheetList() throws Exception {
		
		List<TimesheetResp> timesheetList = new ArrayList<TimesheetResp>();
		
		String timesheetId = "KAN-1-20201129";
		timesheetList.add(new TimesheetResp(timesheetId, "KAN", 1, GenUtils.toDate("2020-11-29"), 3, null, true));
		
		timesheetId = "KAN-1-20201130";
		timesheetList.add(new TimesheetResp(timesheetId, "KAN", 1, GenUtils.toDate("2020-11-30"), 2, null, true));
		
		when(timesheetService.getTimesheetList()).thenReturn(timesheetList);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheets")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].workedHrs").value(3));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetTimesheetListByParams() throws Exception {
		
		String params = "userId=KAN&workedDateFrom=2020-11-29&workedDateTo=";
		
		String userId = "KAN"; 
		LocalDate workedDateFrom = GenUtils.toDate("2020-11-29"); 
		LocalDate workedDateTo = null;
		
		List<TimesheetResp> timesheetList = new ArrayList<TimesheetResp>();
		
		String timesheetId = "KAN-1-20201129";
		timesheetList.add(new TimesheetResp(timesheetId, "KAN", 1, GenUtils.toDate("2020-11-29"), 3, null, true));
		
		timesheetId = "KAN-1-20201130";
		timesheetList.add(new TimesheetResp(timesheetId, "KAN", 1, GenUtils.toDate("2020-11-30"), 2, null, true));
		
		when(timesheetService.getTimesheetList(userId, workedDateFrom, workedDateTo)).thenReturn(timesheetList);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheets?" + params)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[1].workedHrs").value(2));
		
		//---------------------------------------------------------------------
		
		params = "userId=KAN&workedDateFrom=2020-11-29&workedDateTo=2020-11-29";
		
		userId = "KAN"; 
		workedDateFrom = GenUtils.toDate("2020-11-29"); 
		workedDateTo = GenUtils.toDate("2020-11-29");
		
		timesheetId = "KAN-1-20201129";
		timesheetList.clear();
		timesheetList.add(new TimesheetResp(timesheetId, "KAN", 1, GenUtils.toDate("2020-11-29"), 3, null, true));
		
		when(timesheetService.getTimesheetList(userId, workedDateFrom, workedDateTo)).thenReturn(timesheetList);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheets?" + params)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(jsonPath("$[0].workedHrs").value(3));
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testGetTimesheet() throws Exception {
		
		String id = "KAN-1-20201129";
		TimesheetResp timesheet = new TimesheetResp(id, "KAN", 1, GenUtils.toDate("2020-11-29"), 3, null, true);
		
		when(timesheetService.getTimesheet(id)).thenReturn(timesheet);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheets/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.timesheetId").value(id));
	}
	
	@Test
	void testGetTimesheet_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		
		when(timesheetService.getTimesheet(id)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheets/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testAddTimesheet() throws Exception {
		
		String id = "KAN-1-20201201";
		String timesheetReq = "{\"userId\": \"KAN\", \"jobNo\": 1, \"workedDate\": \"2020-12-01\", \"workedHrs\": 3.0, \"timesheetCode\": \"ZZ11\", \"active\": true}";
		
		TimesheetReq timesheetReq2 = new TimesheetReq();
		timesheetReq2.setUserId("KAN");;
		timesheetReq2.setJobNo(1);
		timesheetReq2.setWorkedDate(GenUtils.toDate("2020-12-01"));
		timesheetReq2.setWorkedHrs(3.0);
		timesheetReq2.setTimesheetCode("ZZ11");
		timesheetReq2.setActive(true);
		
		TimesheetResp timesheetResp = new TimesheetResp(id, "KAN", 1, GenUtils.toDate("2020-12-01"), 3, "ZZ11", true);
		
		when(timesheetService.addTimesheet(timesheetReq2)).thenReturn(timesheetResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheets")
				.content(timesheetReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.timesheetId").value(id));
	}
	
	@Test
	void testAddTimesheet_Timesheet_Exists() throws Exception {
		
		String id = "KAN-1-20201129";
		String timesheetReq = "{\"userId\": \"KAN\", \"jobNo\": 1, \"workedDate\": \"2020-11-29\", \"workedHrs\": 3.0, \"timesheetCode\": \"ZZ11\", \"active\": true}";
		
		TimesheetReq timesheetReq2 = new TimesheetReq();
		timesheetReq2.setUserId("KAN");;
		timesheetReq2.setJobNo(1);
		timesheetReq2.setWorkedDate(GenUtils.toDate("2020-11-29"));
		timesheetReq2.setWorkedHrs(3.0);
		timesheetReq2.setTimesheetCode("ZZ11");
		timesheetReq2.setActive(true);
		
		when(timesheetService.addTimesheet(timesheetReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.timesheetExistsMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheets")
				.content(timesheetReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetExistsMsg(id)));
	}
	
	@Test
	void testAddTimesheet_Timesheet_Null() throws Exception {
		
		String timesheetReq = "{\"userId\": null, \"jobNo\": null, \"workedDate\": null, \"workedHrs\": 3.0, \"timesheetCode\": \"ZZ11\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheets")
				.content(timesheetReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTBLANK_TIMESHEET_MSG));
	}
	
	@Test
	void testAddTimesheet_Timesheet_Blank() throws Exception {
		
		String timesheetReq = "{\"userId\": \"\", \"jobNo\": \"\", \"workedDate\": \"\", \"workedHrs\": 3.0, \"timesheetCode\": \"ZZ11\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheets")
				.content(timesheetReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTBLANK_TIMESHEET_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateTimesheet() throws Exception {
		
		String id = "KAN-1-20201129";
		String timesheetReq = "{\"workedHrs\": 5.0, \"timesheetCode\": \"ZZ11\", \"active\": true}";
		
		TimesheetReqUpd timesheetReq2 = new TimesheetReqUpd();
		timesheetReq2.setWorkedHrs(5.0);
		timesheetReq2.setTimesheetCode("ZZ11");
		timesheetReq2.setActive(true);
		
		TimesheetResp timesheetResp = new TimesheetResp(id, "KAN", 1, GenUtils.toDate("2020-11-29"), 5, "ZZ11", true);
		
		when(timesheetService.updateTimesheet(id, timesheetReq2)).thenReturn(timesheetResp);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/timesheets/{id}", id)
				.content(timesheetReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.timesheetCode").value("ZZ11"))
				.andExpect(jsonPath("$.workedHrs").value(5));
	}
	
	@Test
	void testUpdateTimesheet_Timesheet_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		String timesheetReq = "{\"workedHrs\": 5.0, \"timesheetCode\": \"ZZ11\", \"active\": true}";
		
		TimesheetReqUpd timesheetReq2 = new TimesheetReqUpd();
		timesheetReq2.setWorkedHrs(5.0);
		timesheetReq2.setTimesheetCode("ZZ11");
		timesheetReq2.setActive(true);
		
		when(timesheetService.updateTimesheet(id, timesheetReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.timesheetDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/timesheets/Unknown")
				.content(timesheetReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetDoesNotExistMsg("Unknown")));
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteTimesheet() throws Exception {
		
		String id = "KAN-1-20201129";
		
		when(timesheetService.deleteTimesheet(id)).thenReturn(
				new DeleteResp(ValidationUtils.timesheetDeletedMsg(id))
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/timesheets/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetDeletedMsg(id)));
	}
	
	@Test
	void testDeleteTimesheet_Timesheet_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
	
		when(timesheetService.deleteTimesheet(id)).thenThrow(
				new InvalidDataException(
						ValidationUtils.timesheetDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/timesheets/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetDoesNotExistMsg(id)));
	}

}
