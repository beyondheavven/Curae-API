package com.vention.curae.payment.webhook.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.vention.curae.payment.config.StripeProperties;
import com.vention.curae.payment.webhook.dto.WebhookProcessingDto;
import com.vention.curae.payment.webhook.service.WebhookService;
import com.vention.curae.wrapper.ResponseWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebhookControllerTest {

	@Mock
	private StripeProperties stripeProperties;

	@Mock
	private WebhookService webhookService;

	@InjectMocks
	private WebhookController controller;

	@Test
	void getStripeWebhookProcessesVerifiedEvent() throws SignatureVerificationException {
		String payload = "{\"id\":\"evt_1\"}";
		String signature = "t=1,v1=abc";
		Event event = mock(Event.class);
		WebhookProcessingDto processing = new WebhookProcessingDto("evt_1", "type", "ok", "message");
		when(stripeProperties.getWebhookSecret()).thenReturn("secret");
		when(webhookService.processEvent(event)).thenReturn(processing);

		ResponseWrapper<WebhookProcessingDto> result;
		try (MockedStatic<Webhook> webhook = mockStatic(Webhook.class)) {
			webhook.when(() -> Webhook.constructEvent(payload, signature, "secret")).thenReturn(event);
			result = controller.getStripeWebhook(payload, signature);
			webhook.verify(() -> Webhook.constructEvent(payload, signature, "secret"));
		}

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isSameAs(processing);
		verify(webhookService).processEvent(event);
	}

}
