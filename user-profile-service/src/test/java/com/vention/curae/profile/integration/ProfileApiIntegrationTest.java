package com.vention.curae.profile.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		properties = "spring.kafka.listener.auto-startup=false")
@AutoConfigureMockMvc
class ProfileApiIntegrationTest {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void shouldUpdateAndReturnProfileWhenPatientEditsAddress() throws Exception {
		Long addressId = jdbcTemplate.queryForObject(
				"""
						INSERT INTO addresses (street_name, city_name, house_number, state_name, apartment_number, zip_code, phone)
						VALUES ('Old Street', 'Old City', '1', 'CA', '2', '90001', '123')
						RETURNING id
						""",
				Long.class);
		jdbcTemplate.update("""
				INSERT INTO patients (citizenship, gender, user_id, address_id)
				VALUES ('American', 'MALE', 42, ?)
				""", addressId);
		jdbcTemplate.update("""
				INSERT INTO user_snapshots (user_id, email, first_name, last_name, birth_date, status)
				VALUES (42, 'patient@example.com', 'John', 'Doe', DATE '1990-01-01', 'ACTIVE')
				""");

		mockMvc.perform(get("/profile").with(user("42").roles("PATIENT")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.personalDetails.name").value("John"))
			.andExpect(jsonPath("$.data.profileAddress.city").value("Old City"))
			.andExpect(jsonPath("$.data.contactsDto.email").value("patient@example.com"));

		mockMvc
			.perform(put("/profile").with(user("42").roles("PATIENT"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(
						"""
								{"profileAddressUpdateDto":{"street":"New Street","city":"New City","houseNumber":"3","state":"CA","apartment":"4","zipCode":"90002"}}
								"""))
			.andExpect(status().isNoContent());

		mockMvc.perform(get("/profile").with(user("42").roles("PATIENT")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.profileAddress.city").value("New City"))
			.andExpect(jsonPath("$.data.profileAddress.street").value("New Street"));
	}

}
