package com.jtrack.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.jtrack.mapper.LoginHistMapper;
import com.jtrack.model.LoginHist;
import com.jtrack.model.User;

// testing using in memory (H2) database
@DataJpaTest
class LoginHistServiceTest {
	
	@Autowired
	private LoginHistService jobPriorityService;
	
	@Autowired
    private TestEntityManager testEntityManager;
	
	@TestConfiguration
    static class LoginHistServiceConfiguration {
		
		@Bean
		public LoginHistService jobPriorityService() {
			return new LoginHistServiceImpl();
		}
		
		@Bean 
		public LoginHistMapper jobPriorityMapper() {
			return new LoginHistMapper();
		}
    }
	
	@BeforeEach
	void setUp() throws Exception {
		
		testEntityManager.persist(new User("KAN", "kan10", "Kan", "Ranganathan", true, null, "KAN", null, null));
		
		testEntityManager.persist(new LoginHist("KAN-127.0.0.1-1", "KAN", "127.0.0.1", LocalDateTime.now()));
		testEntityManager.persist(new LoginHist("KAN-127.0.0.1-2", "KAN", "127.0.0.1", LocalDateTime.now()));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetLoginHistList() {
		assertThat(jobPriorityService.getLoginHistList()).hasSize(2);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testAddLoginHist() {
		
		String id = "KAN";
		assertThat(jobPriorityService.addLoginHist(id).getUserId()).isEqualTo(id);
	}

}
