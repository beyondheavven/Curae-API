package com.vention.curae.feedback.clinic.controller;

import com.vention.curae.feedback.clinic.dto.ClinicFeedbackCreateDto;
import com.vention.curae.feedback.clinic.dto.ClinicFeedbackSummaryDto;
import com.vention.curae.feedback.clinic.service.ClinicFeedbackService;
import com.vention.curae.wrapper.ResponseWrapper;
import java.security.Principal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class ClinicFeedbackControllerTest {

	@Mock
	private ClinicFeedbackService clinicFeedbackService;

	@InjectMocks
	private ClinicFeedbackController clinicFeedbackController;

	@Test
	void getSummaryReturnsOk() {
		Pageable pageable = Mockito.mock(Pageable.class);
		ClinicFeedbackSummaryDto dto = Mockito.mock(ClinicFeedbackSummaryDto.class);
		Mockito.when(clinicFeedbackService.getFeedbackSummaryByClinicId(1L, pageable)).thenReturn(dto);

		ResponseWrapper<ClinicFeedbackSummaryDto> response = clinicFeedbackController.getSummary(1L, pageable);

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertSame(dto, response.getData());
	}

	@Test
	void createReturnsCreated() {
		Principal principal = Mockito.mock(Principal.class);
		Mockito.when(principal.getName()).thenReturn("1");
		ClinicFeedbackCreateDto dto = Mockito.mock(ClinicFeedbackCreateDto.class);
		Mockito.when(clinicFeedbackService.create(1L, dto)).thenReturn(7L);

		ResponseWrapper<Long> response = clinicFeedbackController.create(principal, dto);

		Assertions.assertEquals(HttpStatus.CREATED, response.getStatus());
		Assertions.assertEquals(Long.valueOf(7L), response.getData());
		Mockito.verify(clinicFeedbackService).create(1L, dto);
	}

}
