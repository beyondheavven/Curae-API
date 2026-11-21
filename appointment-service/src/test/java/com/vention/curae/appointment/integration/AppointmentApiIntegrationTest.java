package com.vention.curae.appointment.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import java.sql.Time;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		properties = "spring.kafka.listener.auto-startup=false")
@AutoConfigureMockMvc
class AppointmentApiIntegrationTest {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void shouldCreateReferralAndFindCompletedAppointmentWhenDoctorRequestsIt() throws Exception {
		Long patientId = 42L;
		Long doctorId = 7L;

		String requestBody = """
				{"patientId":42,"serviceId":101,"appointmentType":"VISIT","expiresAt":"%s"}
				""".formatted(LocalDate.now().plusYears(1));

		MvcResult createResult = mockMvc
			.perform(post("/prescriptions").with(user("7").roles("DOCTOR"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.status").value("201 CREATED"))
			.andExpect(jsonPath("$.data").isNumber())
			.andReturn();

		Long referralId = ((Number) JsonPath.read(createResult.getResponse().getContentAsString(), "$.data"))
			.longValue();

		jdbcTemplate.update("""
				INSERT INTO appointments (scheduled_date, scheduled_time, status, patient_id, type, referral_id)
				VALUES (?, ?, 'COMPLETED', ?, 'VISIT', ?)
				""", LocalDate.now().plusDays(1), Time.valueOf("10:00:00"), patientId, referralId);

		mockMvc
			.perform(get("/appointments/internal/completed").param("patientId", patientId.toString())
				.param("doctorId", doctorId.toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("200 OK"))
			.andExpect(jsonPath("$.data").value(true));
	}

}
