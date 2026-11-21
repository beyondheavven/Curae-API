package com.vention.curae.auth.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		properties = "spring.kafka.listener.auto-startup=false")
@AutoConfigureMockMvc
class AuthApiIntegrationTest {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void shouldAuthenticateAndReturnPersistedUserWhenCredentialsAreValid() throws Exception {
		String email = "admin@integration.curae";
		Long userId = jdbcTemplate.queryForObject("""
				INSERT INTO users (email, password_hash, first_name, last_name, role, date_of_birth, status)
				VALUES (?, ?, 'Ada', 'Admin', 'ADMIN', DATE '1990-01-01', 'ACTIVE')
				RETURNING id
				""", Long.class, email, passwordEncoder.encode("StrongPass1!"));

		MvcResult loginResult = mockMvc
			.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"email\":\"" + email + "\",\"password\":\"StrongPass1!\"}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("200 OK"))
			.andExpect(jsonPath("$.data.accessToken").isNotEmpty())
			.andExpect(jsonPath("$.data.refreshToken").isNotEmpty())
			.andReturn();

		String accessToken = JsonPath.read(loginResult.getResponse().getContentAsString(), "$.data.accessToken");

		mockMvc.perform(get("/users/{id}", userId).header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").value(userId))
			.andExpect(jsonPath("$.data.email").value(email))
			.andExpect(jsonPath("$.data.role").value("ADMIN"));

		Integer tokenCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM refresh_tokens WHERE users_id = ?",
				Integer.class, userId);
		assertThat(tokenCount).isEqualTo(1);
	}

}
