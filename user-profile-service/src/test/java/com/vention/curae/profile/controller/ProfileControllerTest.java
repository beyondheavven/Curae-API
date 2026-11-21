package com.vention.curae.profile.controller;

import com.vention.curae.profile.dto.request.ProfileRequestDto;
import com.vention.curae.profile.dto.response.ProfileResponseDto;
import com.vention.curae.profile.service.ProfileService;
import com.vention.curae.wrapper.ResponseWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

	@Mock
	private ProfileService profileService;

	@InjectMocks
	private ProfileController controller;

	@Test
	void getProfileReturnsProfileForAuthenticatedUser() {
		Authentication authentication = mock(Authentication.class);
		ProfileResponseDto profile = mock(ProfileResponseDto.class);
		when(authentication.getName()).thenReturn("1");
		when(profileService.getProfile(1L)).thenReturn(profile);

		ResponseWrapper<ProfileResponseDto> result = controller.getProfile(authentication);

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isSameAs(profile);
		verify(profileService).getProfile(1L);
	}

	@Test
	void updateDelegatesToServiceForAuthenticatedUser() {
		Authentication authentication = mock(Authentication.class);
		ProfileRequestDto request = mock(ProfileRequestDto.class);
		when(authentication.getName()).thenReturn("1");

		controller.update(authentication, request);

		verify(profileService).updateProfile(1L, request);
	}

}
