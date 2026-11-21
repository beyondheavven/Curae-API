package com.vention.curae.provider.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		properties = "spring.kafka.listener.auto-startup=false")
@AutoConfigureMockMvc
class ProviderApiIntegrationTest {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldCreateAndRetrieveClinicWhenAdminSubmitsIt() throws Exception {
		MvcResult createResult = mockMvc
			.perform(post("/clinics").with(user("1").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(
						"""
								{"name":"Integration Clinic","logoUrl":"https://example.com/logo.png","description":"E2E clinic","address":{"street":"Main St","city":"Kyiv","houseNumber":"10","state":"UA","apartment":"5","zipCode":"01001","phone":"+380000000000"}}
								"""))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.status").value("201 CREATED"))
			.andExpect(jsonPath("$.data").isNumber())
			.andReturn();

		Long clinicId = ((Number) JsonPath.read(createResult.getResponse().getContentAsString(), "$.data")).longValue();

		mockMvc.perform(get("/clinics/{id}", clinicId).with(user("1").roles("ADMIN")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.name").value("Integration Clinic"))
			.andExpect(jsonPath("$.data.address.city").value("Kyiv"))
			.andExpect(jsonPath("$.data.address.street").value("Main St"));
	}

}
