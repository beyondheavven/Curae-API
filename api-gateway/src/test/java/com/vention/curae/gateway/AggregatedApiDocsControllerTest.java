package com.vention.curae.gateway;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AggregatedApiDocsControllerTest {

	@Mock
	private GatewayProperties properties;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private RestClient.Builder restClientBuilder;

	@Test
	@SuppressWarnings("unchecked")
	void aggregatedDocsMergesDocumentsAndHandlesFailures() {
		RestClient restClient = mock(RestClient.class, RETURNS_DEEP_STUBS);
		when(restClientBuilder.build()).thenReturn(restClient);

		GatewayProperties.Service healthy = new GatewayProperties.Service();
		healthy.setName("auth");
		healthy.setUrl("http://auth");
		GatewayProperties.Service nullBody = new GatewayProperties.Service();
		nullBody.setName("null");
		nullBody.setUrl("http://null");
		GatewayProperties.Service failing = new GatewayProperties.Service();
		failing.setName("fail");
		failing.setUrl("http://fail");
		when(properties.getServices()).thenReturn(List.of(healthy, nullBody, failing));

		Map<String, Object> doc = Map.of("paths", Map.of("/a", Map.of("get", Map.of())), "components",
				Map.of("schemas", Map.of("A", Map.of("type", "object"))), "tags", List.of(Map.of("name", "Auth")));
		when(restClient.get().uri(anyString()).retrieve().body(any(ParameterizedTypeReference.class)))
			.thenReturn(doc, null)
			.thenThrow(new RuntimeException("boom"));

		AggregatedApiDocsController controller = new AggregatedApiDocsController(properties, restClientBuilder);
		ResponseEntity<Map<String, Object>> response = controller.aggregatedDocs();

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isNotNull();
		Map<String, Object> paths = (Map<String, Object>) response.getBody().get("paths");
		assertThat(paths).containsKey("/a");
		Map<String, Object> components = (Map<String, Object>) response.getBody().get("components");
		Map<String, Object> schemas = (Map<String, Object>) components.get("schemas");
		assertThat(schemas).containsKey("A");
		assertThat((List<?>) response.getBody().get("tags")).hasSize(1);
	}

	@Test
	void aggregatedDocsHandlesNonMapAndNonListBranches() {
		RestClient restClient = mock(RestClient.class, RETURNS_DEEP_STUBS);
		when(restClientBuilder.build()).thenReturn(restClient);

		GatewayProperties.Service service = new GatewayProperties.Service();
		service.setName("odd");
		service.setUrl("http://odd");
		when(properties.getServices()).thenReturn(List.of(service));

		Map<String, Object> doc = Map.of("paths", "bad", "components", Map.of("schemas", "bad"), "tags", "bad");
		when(restClient.get().uri(anyString()).retrieve().body(any(ParameterizedTypeReference.class))).thenReturn(doc);

		AggregatedApiDocsController controller = new AggregatedApiDocsController(properties, restClientBuilder);
		ResponseEntity<Map<String, Object>> response = controller.aggregatedDocs();

		assertThat((Map<?, ?>) response.getBody().get("paths")).isEmpty();
		Map<?, ?> components = (Map<?, ?>) response.getBody().get("components");
		assertThat((Map<?, ?>) components.get("schemas")).isEmpty();
		assertThat((List<?>) response.getBody().get("tags")).isEmpty();
	}

}
