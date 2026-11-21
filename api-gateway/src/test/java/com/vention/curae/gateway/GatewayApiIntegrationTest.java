package com.vention.curae.gateway;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		properties = {
				"spring.autoconfigure.exclude=org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,"
						+ "org.springframework.boot.jdbc.autoconfigure.DataSourceInitializationAutoConfiguration,"
						+ "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration,"
						+ "org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration" })
@AutoConfigureMockMvc
class GatewayApiIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnUnauthorizedWhenProtectedRouteHasNoToken() throws Exception {
		mockMvc.perform(get("/integration-protected")).andExpect(status().isUnauthorized());
	}

	@Test
	void shouldPassFilterWhenProtectedRouteHasValidToken() throws Exception {
		SecretKey key = Keys
			.hmacShaKeyFor("very-long-secret-should-be-at-least-256-bit".getBytes(StandardCharsets.UTF_8));
		String token = Jwts.builder().subject("42").claim("role", "PATIENT").signWith(key).compact();

		mockMvc.perform(get("/integration-protected").header("Authorization", "Bearer " + token))
			.andExpect(status().isNotFound());
	}

	@Test
	void shouldExposeHealthWhenRouteIsPublic() throws Exception {
		mockMvc.perform(get("/actuator/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").exists());
	}

}
