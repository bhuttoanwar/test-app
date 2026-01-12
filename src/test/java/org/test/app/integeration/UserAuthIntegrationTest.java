package org.test.app.integeration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserAuthIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void login_shouldReturnToken() throws Exception {

		String json = """
				{
				  "username": "admin-usr",
				  "password": "admin123"
				}
				""";

		mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andExpect(jsonPath("data").exists());
	}

	@Test
	void login_shouldFail_whenCredentialsInvalid() throws Exception {

		String json = """
				{
				  "username": "admin-test",
				  "password": "admin123"
				}
				""";

		mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound()).andExpect(jsonPath("message").exists());
	}
}
