package com.vention.curae.user.controller;

import com.vention.curae.email.service.EmailVerificationTokenService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailVerificationControllerTest {

	@Mock
	private EmailVerificationTokenService emailVerificationTokenService;

	@InjectMocks
	private EmailVerificationController emailVerificationController;

	@Test
	void verifyDelegatesToService() {
		emailVerificationController.verify("token-value");

		verify(emailVerificationTokenService).verify("token-value");
	}

}
