package com.vention.curae.feedback.doctor.controller;

import com.vention.curae.feedback.doctor.dto.DoctorFeedbackCreateDto;
import com.vention.curae.feedback.doctor.dto.DoctorFeedbackSummaryDto;
import com.vention.curae.feedback.doctor.service.DoctorFeedbackService;
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
class DoctorFeedbackControllerTest {

	@Mock
	private DoctorFeedbackService doctorFeedbackService;

	@InjectMocks
	private DoctorFeedbackController doctorFeedbackController;

	@Test
	void getSummaryReturnsOk() {
		Pageable pageable = Mockito.mock(Pageable.class);
		DoctorFeedbackSummaryDto dto = Mockito.mock(DoctorFeedbackSummaryDto.class);
		Mockito.when(doctorFeedbackService.getFeedbackSummaryByDoctorId(1L, pageable)).thenReturn(dto);

		ResponseWrapper<DoctorFeedbackSummaryDto> response = doctorFeedbackController.getSummary(1L, pageable);

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertSame(dto, response.getData());
	}

	@Test
	void createReturnsCreated() {
		Principal principal = Mockito.mock(Principal.class);
		Mockito.when(principal.getName()).thenReturn("1");
		DoctorFeedbackCreateDto dto = Mockito.mock(DoctorFeedbackCreateDto.class);
		Mockito.when(doctorFeedbackService.create(1L, dto)).thenReturn(7L);

		ResponseWrapper<Long> response = doctorFeedbackController.create(principal, dto);

		Assertions.assertEquals(HttpStatus.CREATED, response.getStatus());
		Assertions.assertEquals(Long.valueOf(7L), response.getData());
		Mockito.verify(doctorFeedbackService).create(1L, dto);
	}

}
