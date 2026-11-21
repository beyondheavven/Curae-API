package com.vention.curae.auth.controller;

import com.vention.curae.auth.dto.AuthRequestDto;
import com.vention.curae.auth.dto.AuthResponseDto;
import com.vention.curae.auth.dto.LogoutRequestDto;
import com.vention.curae.auth.dto.RefreshTokenRequestDto;
import com.vention.curae.auth.service.AuthService;
import com.vention.curae.wrapper.ResponseWrapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	@Mock
	private AuthService authService;

	@InjectMocks
	private AuthController authController;

	@Test
	void loginReturnsResponseWrapper() {
		AuthRequestDto request = new AuthRequestDto("user@test.com", "pass");
		AuthResponseDto response = new AuthResponseDto("access", "refresh");
		when(authService.login(request)).thenReturn(response);

		ResponseWrapper<AuthResponseDto> result = authController.login(request);

		assertThat(result.getData()).isEqualTo(response);
		verify(authService).login(request);
	}

	@Test
	void refreshReturnsResponseWrapper() {
		RefreshTokenRequestDto request = new RefreshTokenRequestDto("refresh-token");
		AuthResponseDto response = new AuthResponseDto("new-access", "new-refresh");
		when(authService.refresh("refresh-token")).thenReturn(response);

		ResponseWrapper<AuthResponseDto> result = authController.refresh(request);

		assertThat(result.getData()).isEqualTo(response);
		verify(authService).refresh("refresh-token");
	}

	@Test
	void logoutDelegatesToService() {
		LogoutRequestDto request = new LogoutRequestDto("refresh-token");

		authController.logout(request);

		verify(authService).logout("refresh-token");
	}

}
