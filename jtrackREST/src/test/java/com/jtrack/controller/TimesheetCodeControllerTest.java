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
import com.jtrack.dto.TimesheetCodeReq;
import com.jtrack.dto.TimesheetCodeResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.TimesheetCodeService;
import com.jtrack.validation.ValidationUtils;

@ActiveProfiles("test")
@WebMvcTest(TimesheetCodeController.class)
class TimesheetCodeControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private TimesheetCodeService timesheetCodeService;
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetTimesheetCodeList() throws Exception {
		
		List<TimesheetCodeResp> timesheetCodeList = new ArrayList<TimesheetCodeResp>();
		timesheetCodeList.add(new TimesheetCodeResp("ZZ01", "Reviews/Meetings", true));
		timesheetCodeList.add(new TimesheetCodeResp("ZZ11", "Sales Support", true));
		
		when(timesheetCodeService.getTimesheetCodeList()).thenReturn(timesheetCodeList);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheetCodes")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].timesheetCode").value("ZZ01"));
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testGetTimesheetCode() throws Exception {
		
		String id = "ZZ01";
		TimesheetCodeResp timesheetCode = new TimesheetCodeResp(id, "Reviews/Meetings", true);
		
		when(timesheetCodeService.getTimesheetCode(id)).thenReturn(timesheetCode);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheetCodes/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.timesheetCode").value(id));
	}
	
	@Test
	void testGetTimesheetCode_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		
		when(timesheetCodeService.getTimesheetCode(id)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheetCodes/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testAddTimesheetCode() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"TesT - 1_2\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		TimesheetCodeReq timesheetCodeReq2 = new TimesheetCodeReq();
		timesheetCodeReq2.setTimesheetCode("TesT - 1_2");
		timesheetCodeReq2.setTimesheetCodeDesc("Test");
		timesheetCodeReq2.setActive(true);
		
		TimesheetCodeResp TimesheetCodeResp = new TimesheetCodeResp("TesT - 1_2", "Test", true);
		
		when(timesheetCodeService.addTimesheetCode(timesheetCodeReq2)).thenReturn(TimesheetCodeResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.timesheetCode").value("TesT - 1_2"));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_Exists() throws Exception {
		
		String id = "ZZ01";
		String timesheetCodeReq = "{\"timesheetCode\": \"" + id + "\", \"timesheetCodeDesc\": \"Reviews\", \"active\": true}";
		
		TimesheetCodeReq timesheetCodeReq2 = new TimesheetCodeReq();
		timesheetCodeReq2.setTimesheetCode(id);
		timesheetCodeReq2.setTimesheetCodeDesc("Reviews");
		timesheetCodeReq2.setActive(true);
		
		when(timesheetCodeService.addTimesheetCode(timesheetCodeReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.timesheetCodeExistsMsg(timesheetCodeReq2.getTimesheetCode())
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetCodeExistsMsg(id)));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_Null() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": null, \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_TIMESHEET_CODE_MSG));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_Empty() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_TIMESHEET_CODE_MSG));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_Blank() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \" \", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_TIMESHEET_CODE_MSG));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_Start_With_Invalid_Char() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"123\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_TIMESHEET_CODE_MSG));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_End_With_Invalid_Char() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"T1@\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_TIMESHEET_CODE_MSG));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"TEST@10\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_TIMESHEET_CODE_MSG));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_With_1_Char() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"T\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_TIMESHEET_CODE_MSG));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_With_2_Chars() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"T1\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		TimesheetCodeReq timesheetCodeReq2 = new TimesheetCodeReq();
		timesheetCodeReq2.setTimesheetCode("T1");
		timesheetCodeReq2.setTimesheetCodeDesc("Test");
		timesheetCodeReq2.setActive(true);
		
		TimesheetCodeResp TimesheetCodeResp = new TimesheetCodeResp("T1", "Test", true);
		
		when(timesheetCodeService.addTimesheetCode(timesheetCodeReq2)).thenReturn(TimesheetCodeResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.timesheetCode").value("T1"));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_With_20_Chars() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"T123456789T123456789\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		TimesheetCodeReq timesheetCodeReq2 = new TimesheetCodeReq();
		timesheetCodeReq2.setTimesheetCode("T123456789T123456789");
		timesheetCodeReq2.setTimesheetCodeDesc("Test");
		timesheetCodeReq2.setActive(true);
		
		TimesheetCodeResp TimesheetCodeResp = new TimesheetCodeResp("T123456789T123456789", "Test", true);
		
		when(timesheetCodeService.addTimesheetCode(timesheetCodeReq2)).thenReturn(TimesheetCodeResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.timesheetCode").value("T123456789T123456789"));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_With_21_Chars() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"T123456789T1234567891\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_TIMESHEET_CODE_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateTimesheetCode() throws Exception {
		
		String id = "ZZ01";
		String timesheetCodeReq = "{\"timesheetCodeDesc\": \"Testing\", \"active\": true}";
		
		TimesheetCodeReq timesheetCodeReq2 = new TimesheetCodeReq();
		timesheetCodeReq2.setTimesheetCodeDesc("Testing");
		timesheetCodeReq2.setActive(true);
		
		TimesheetCodeResp TimesheetCodeResp = new TimesheetCodeResp(id, "Testing", true);
		
		when(timesheetCodeService.updateTimesheetCode(id, timesheetCodeReq2)).thenReturn(TimesheetCodeResp);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/timesheetCodes/{id}", id)
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.timesheetCodeDesc").value("Testing"));
	}
	
	@Test
	void testUpdateTimesheetCode_TimesheetCode_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		String timesheetCodeReq = "{\"timesheetCodeDesc\": \"Testing\", \"active\": true}";
		
		TimesheetCodeReq timesheetCodeReq2 = new TimesheetCodeReq();
		timesheetCodeReq2.setTimesheetCodeDesc("Testing");
		timesheetCodeReq2.setActive(true);
		
		when(timesheetCodeService.updateTimesheetCode(id, timesheetCodeReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.timesheetCodeDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/timesheetCodes/{id}", id)
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetCodeDoesNotExistMsg(id)));
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteTimesheetCode() throws Exception {
		
		String id = "ZZ01";
		
		when(timesheetCodeService.deleteTimesheetCode(id)).thenReturn(
				new DeleteResp(ValidationUtils.timesheetCodeDeletedMsg(id))
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/timesheetCodes/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetCodeDeletedMsg(id)));
	}
	
	@Test
	void testDeleteTimesheetCode_TimesheetCode_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		
		when(timesheetCodeService.deleteTimesheetCode(id)).thenThrow(
				new InvalidDataException(
						ValidationUtils.timesheetCodeDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/timesheetCodes/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetCodeDoesNotExistMsg(id)));
	}

}
