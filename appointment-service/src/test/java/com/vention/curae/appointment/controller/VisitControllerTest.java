package com.vention.curae.appointment.controller;

import com.vention.curae.appointment.dto.VisitCreateDto;
import com.vention.curae.appointment.dto.VisitListItemDto;
import com.vention.curae.appointment.model.VisitType;
import com.vention.curae.appointment.security.AppointmentSecurityService;
import com.vention.curae.appointment.service.VisitService;
import com.vention.curae.wrapper.ResponseWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisitControllerTest {

	@Mock
	private VisitService visitService;

	@Mock
	private AppointmentSecurityService securityService;

	@InjectMocks
	private VisitController controller;

	@Test
	void createReturnsCreatedStatus() {
		VisitCreateDto dto = mock(VisitCreateDto.class);
		when(visitService.create(2L, dto)).thenReturn(8L);

		ResponseWrapper<Long> result = controller.create(dto, 2L);

		assertThat(result.getStatus()).isEqualTo(HttpStatus.CREATED);
		assertThat(result.getData()).isEqualTo(8L);
		verify(visitService).create(2L, dto);
	}

	@Test
	void getAllReturnsVisitsForCurrentPatient() {
		Pageable pageable = PageRequest.of(0, 20);
		Page<VisitListItemDto> page = Page.empty();
		when(securityService.currentPatientId()).thenReturn(2L);
		when(visitService.findAllForPatient(2L, VisitType.UPCOMING, pageable)).thenReturn(page);

		ResponseWrapper<Page<VisitListItemDto>> result = controller.getAll(VisitType.UPCOMING, pageable);

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isSameAs(page);
		verify(securityService).currentPatientId();
		verify(visitService).findAllForPatient(2L, VisitType.UPCOMING, pageable);
	}

}
