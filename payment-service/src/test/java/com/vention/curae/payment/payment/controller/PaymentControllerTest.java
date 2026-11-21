package com.vention.curae.payment.payment.controller;

import com.stripe.exception.StripeException;
import com.vention.curae.payment.payment.dto.PaymentRequestDto;
import com.vention.curae.payment.payment.service.PaymentService;
import com.vention.curae.wrapper.ResponseWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

	@Mock
	private PaymentService paymentService;

	@InjectMocks
	private PaymentController controller;

	@Test
	void createCheckoutSessionReturnsSessionUrl() throws StripeException {
		PaymentRequestDto requestDto = mock(PaymentRequestDto.class);
		when(paymentService.createCheckoutSession(requestDto)).thenReturn("https://checkout.stripe.com/session");

		ResponseWrapper<String> result = controller.createCheckoutSession(requestDto);

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isEqualTo("https://checkout.stripe.com/session");
		verify(paymentService).createCheckoutSession(requestDto);
	}

}
