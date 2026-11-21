package com.vention.curae.password.controller;

import com.vention.curae.auth.core.CustomUserDetails;
import com.vention.curae.password.dto.ChangePasswordRequestDto;
import com.vention.curae.password.dto.PasswordResetConfirmDto;
import com.vention.curae.password.dto.PasswordResetRequestDto;
import com.vention.curae.password.dto.PasswordResetVerifyDto;
import com.vention.curae.password.dto.PasswordResetVerifyResponseDto;
import com.vention.curae.password.dto.SetPasswordRequestDto;
import com.vention.curae.password.service.PasswordManagementService;
import com.vention.curae.password.service.PasswordResetService;
import com.vention.curae.wrapper.ResponseWrapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordControllerTest {

	@Mock
	private PasswordManagementService passwordManagementService;

	@Mock
	private PasswordResetService passwordResetService;

	@InjectMocks
	private PasswordController passwordController;

	@Test
	void changePasswordDelegatesAndReturnsRequest() {
		ChangePasswordRequestDto request = new ChangePasswordRequestDto("current", "newpassword", "newpassword");
		CustomUserDetails userDetails = mock(CustomUserDetails.class);
		when(userDetails.getId()).thenReturn(42L);

		ResponseWrapper<ChangePasswordRequestDto> result = passwordController.changePassword(request, userDetails);

		assertThat(result.getData()).isEqualTo(request);
		verify(passwordManagementService).changePassword(42L, "current", "newpassword", "newpassword");
	}

	@Test
	void requestResetSendsCodeAndReturnsRequest() {
		PasswordResetRequestDto request = new PasswordResetRequestDto();
		request.setEmail("user@test.com");

		ResponseWrapper<PasswordResetRequestDto> result = passwordController.requestReset(request);

		assertThat(result.getData()).isEqualTo(request);
		verify(passwordResetService).sendResetCode("user@test.com");
	}

	@Test
	void verifyCodeReturnsResetToken() {
		PasswordResetVerifyDto request = new PasswordResetVerifyDto();
		request.setEmail("user@test.com");
		request.setCode("123456");
		when(passwordResetService.verifyCode("user@test.com", "123456")).thenReturn("reset-token");

		ResponseWrapper<PasswordResetVerifyResponseDto> result = passwordController.verifyCode(request);

		assertThat(result.getData().resetToken()).isEqualTo("reset-token");
		verify(passwordResetService).verifyCode("user@test.com", "123456");
	}

	@Test
	void confirmResetDelegatesAndReturnsRequest() {
		PasswordResetConfirmDto request = new PasswordResetConfirmDto();
		request.setResetToken("reset-token");
		request.setNewPassword("newpassword");
		request.setRepeatPassword("newpassword");

		ResponseWrapper<PasswordResetConfirmDto> result = passwordController.confirmReset(request);

		assertThat(result.getData()).isEqualTo(request);
		verify(passwordManagementService).resetPassword("reset-token", "newpassword", "newpassword");
	}

	@Test
	void setPasswordDelegatesToService() {
		SetPasswordRequestDto request = mock(SetPasswordRequestDto.class);

		passwordController.setPassword(request);

		verify(passwordManagementService).setPassword(request);
	}

}
