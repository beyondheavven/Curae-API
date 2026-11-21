package com.vention.curae.notification.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		properties = "spring.kafka.listener.auto-startup=false")
@AutoConfigureMockMvc
class NotificationApiIntegrationTest {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void shouldMarkNotificationAsReadWhenPatientRequestsIt() throws Exception {
		Long notificationId = jdbcTemplate.queryForObject("""
				INSERT INTO notifications (patient_id, appointment_id, type, status)
				VALUES (?, ?, 'APPOINTMENT_CREATED', 'UNREAD')
				RETURNING id
				""", Long.class, 42L, 99L);

		mockMvc.perform(get("/notifications").with(user("42").roles("PATIENT")).param("page", "0").param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.content[0].id").value(notificationId))
			.andExpect(jsonPath("$.data.content[0].status").value("UNREAD"));

		mockMvc.perform(patch("/notifications/{id}/read", notificationId).with(user("42").roles("PATIENT")))
			.andExpect(status().isNoContent());

		mockMvc.perform(get("/notifications/{id}", notificationId).with(user("42").roles("PATIENT")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.status").value("READ"));
	}

}
