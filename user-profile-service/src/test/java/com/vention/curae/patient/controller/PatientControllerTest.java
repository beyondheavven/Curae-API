package com.vention.curae.patient.controller;

import com.vention.curae.patient.dto.PatientResponseDto;
import com.vention.curae.patient.service.PatientService;
import com.vention.curae.wrapper.ResponseWrapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

	@Mock
	private PatientService patientService;

	@InjectMocks
	private PatientController controller;

	@Test
	void getByIdAllowsAdminWithoutOwnershipCheck() {
		Authentication authentication = authenticationWithAuthority("ROLE_ADMIN");
		PatientResponseDto patient = mock(PatientResponseDto.class);
		when(patientService.getPatientById(1L)).thenReturn(patient);

		ResponseWrapper<PatientResponseDto> result = controller.getById(1L, authentication);

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isSameAs(patient);
		verify(patientService).getPatientById(1L);
		verify(patientService, never()).isOwnedByUser(anyLong(), anyLong());
	}

	@Test
	void getByIdAllowsDoctorWithoutOwnershipCheck() {
		Authentication authentication = authenticationWithAuthority("ROLE_DOCTOR");
		PatientResponseDto patient = mock(PatientResponseDto.class);
		when(patientService.getPatientById(1L)).thenReturn(patient);

		ResponseWrapper<PatientResponseDto> result = controller.getById(1L, authentication);

		assertThat(result.getData()).isSameAs(patient);
		verify(patientService).getPatientById(1L);
		verify(patientService, never()).isOwnedByUser(anyLong(), anyLong());
	}

	@Test
	void getByIdAllowsOwner() {
		Authentication authentication = authenticationWithAuthority("ROLE_USER");
		PatientResponseDto patient = mock(PatientResponseDto.class);
		when(authentication.getName()).thenReturn("1");
		when(patientService.isOwnedByUser(2L, 1L)).thenReturn(true);
		when(patientService.getPatientById(2L)).thenReturn(patient);

		ResponseWrapper<PatientResponseDto> result = controller.getById(2L, authentication);

		assertThat(result.getData()).isSameAs(patient);
		verify(patientService).isOwnedByUser(2L, 1L);
		verify(patientService).getPatientById(2L);
	}

	@Test
	void getByIdRejectsNonOwner() {
		Authentication authentication = authenticationWithAuthority("ROLE_USER");
		when(authentication.getName()).thenReturn("1");
		when(patientService.isOwnedByUser(2L, 1L)).thenReturn(false);

		assertThatThrownBy(() -> controller.getById(2L, authentication)).isInstanceOf(AccessDeniedException.class);
		verify(patientService).isOwnedByUser(2L, 1L);
		verify(patientService, never()).getPatientById(2L);
	}

	private Authentication authenticationWithAuthority(String authority) {
		GrantedAuthority grantedAuthority = mock(GrantedAuthority.class);
		Authentication authentication = mock(Authentication.class);
		when(grantedAuthority.getAuthority()).thenReturn(authority);
		doReturn(List.of(grantedAuthority)).when(authentication).getAuthorities();
		return authentication;
	}

}
