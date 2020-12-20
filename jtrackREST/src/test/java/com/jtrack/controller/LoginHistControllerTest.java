package com.jtrack.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
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

import com.jtrack.dto.LoginHistResp;
import com.jtrack.service.LoginHistService;

@ActiveProfiles("test")
@WebMvcTest(LoginHistController.class)
class LoginHistControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private LoginHistService loginHistService;
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetLoginHistList() throws Exception {
		
		List<LoginHistResp> loginHistList = new ArrayList<LoginHistResp>();
		loginHistList.add(new LoginHistResp("KAN-127.0.0.1-1", "KAN", "127.0.0.1", LocalDateTime.now()));
		loginHistList.add(new LoginHistResp("KAN-127.0.0.1-2", "KAN", "127.0.0.1", LocalDateTime.now()));
		
		when(loginHistService.getLoginHistList()).thenReturn(loginHistList);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/loginHist")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[1].histId").value("KAN-127.0.0.1-2"));
	}

}
