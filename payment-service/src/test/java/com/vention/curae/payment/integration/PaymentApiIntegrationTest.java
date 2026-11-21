package com.vention.curae.payment.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
class PaymentApiIntegrationTest {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void shouldReturnPurchaseReportWhenAdminRequestsSeededTransactions() throws Exception {
		jdbcTemplate.update("""
				INSERT INTO purchases (patient_id, service_id, stripe_payment_intent_id, amount, currency, status)
				VALUES (?, ?, ?, ?, 'usd', 'SUCCESS')
				""", 11L, 1L, "pi_integration_success", new BigDecimal("150.00"));
		jdbcTemplate.update("""
				INSERT INTO purchases (patient_id, service_id, stripe_payment_intent_id, amount, currency, status)
				VALUES (?, ?, ?, ?, 'usd', 'FAILED')
				""", 12L, 2L, "pi_integration_failed", new BigDecimal("50.00"));

		mockMvc
			.perform(get("/purchase").with(user("1").roles("ADMIN"))
				.param("from", LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
				.param("to", LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("200 OK"))
			.andExpect(jsonPath("$.data.completedPurchasesCount").value(1))
			.andExpect(jsonPath("$.data.canceledPurchasesCount").value(1))
			.andExpect(jsonPath("$.data.totalAmount").value(200))
			.andExpect(jsonPath("$.data.transactions.content.length()").value(2));
	}

}
