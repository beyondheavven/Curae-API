package com.vention.curae.appointment.controller;

import com.vention.curae.appointment.dto.ReferralCreateDto;
import com.vention.curae.appointment.dto.ReferralResponseDto;
import com.vention.curae.appointment.security.AppointmentSecurityService;
import com.vention.curae.appointment.service.ReferralService;
import com.vention.curae.wrapper.ResponseWrapper;
import java.util.List;
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
class ReferralControllerTest {

	@Mock
	private ReferralService referralService;

	@Mock
	private AppointmentSecurityService securityService;

	@InjectMocks
	private ReferralController controller;

	@Test
	void createReturnsCreatedStatus() {
		ReferralCreateDto dto = mock(ReferralCreateDto.class);
		when(referralService.create(dto)).thenReturn(3L);

		ResponseWrapper<Long> result = controller.create(dto);

		assertThat(result.getStatus()).isEqualTo(HttpStatus.CREATED);
		assertThat(result.getData()).isEqualTo(3L);
		verify(referralService).create(dto);
	}

	@Test
	void getActiveUsesCurrentPatient() {
		List<ReferralResponseDto> referrals = List.of();
		when(securityService.currentPatientId()).thenReturn(9L);
		when(referralService.getActiveByPatient(9L)).thenReturn(referrals);

		ResponseWrapper<List<ReferralResponseDto>> result = controller.getActive();

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isSameAs(referrals);
		verify(securityService).currentPatientId();
		verify(referralService).getActiveByPatient(9L);
	}

	@Test
	void markUsedDelegatesToService() {
		controller.markUsed(4L);

		verify(referralService).markUsed(4L);
	}

}
