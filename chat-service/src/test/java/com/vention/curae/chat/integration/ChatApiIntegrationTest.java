package com.vention.curae.chat.integration;

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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "app.rabbit.enabled=false")
@AutoConfigureMockMvc
class ChatApiIntegrationTest {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldCreateAndListChatWhenPatientRequestsIt() throws Exception {
		MvcResult createResult = mockMvc
			.perform(post("/chats").with(user("1").roles("PATIENT"))
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"doctorId\":2}"))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.status").value("201 CREATED"))
			.andExpect(jsonPath("$.data.patientId").value(1))
			.andExpect(jsonPath("$.data.doctorId").value(2))
			.andReturn();

		Long chatId = ((Number) JsonPath.read(createResult.getResponse().getContentAsString(), "$.data.id"))
			.longValue();

		mockMvc.perform(get("/chats").with(user("1").roles("PATIENT")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].id").value(chatId));
	}

}
