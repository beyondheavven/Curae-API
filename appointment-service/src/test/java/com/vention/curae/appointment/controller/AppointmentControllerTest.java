package com.vention.curae.appointment.controller;

import com.vention.curae.appointment.dto.AppointmentCreateDto;
import com.vention.curae.appointment.dto.AppointmentUpdateDto;
import com.vention.curae.appointment.model.AppointmentStatus;
import com.vention.curae.appointment.service.AppointmentService;
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
class AppointmentControllerTest {

	@Mock
	private AppointmentService appointmentService;

	@InjectMocks
	private AppointmentController controller;

	@Test
	void createReturnsCreatedStatus() {
		AppointmentCreateDto dto = mock(AppointmentCreateDto.class);
		when(appointmentService.create(dto)).thenReturn(10L);

		ResponseWrapper<Long> result = controller.create(dto);

		assertThat(result.getStatus()).isEqualTo(HttpStatus.CREATED);
		assertThat(result.getData()).isEqualTo(10L);
		verify(appointmentService).create(dto);
	}

	@Test
	void confirmDelegatesToService() {
		controller.confirm(1L);

		verify(appointmentService).confirm(1L);
	}

	@Test
	void cancelDelegatesToService() {
		controller.cancel(1L);

		verify(appointmentService).cancelAppointment(1L);
	}

	@Test
	void updateReturnsUpdatedId() {
		AppointmentUpdateDto dto = mock(AppointmentUpdateDto.class);

		ResponseWrapper<Long> result = controller.update(1L, dto);

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isEqualTo(1L);
		verify(appointmentService).update(1L, dto);
	}

	@Test
	void existsCompletedDelegatesToService() {
		when(appointmentService.existsByPatientIdDoctorIdAndStatus(2L, 3L, AppointmentStatus.COMPLETED))
			.thenReturn(true);

		ResponseWrapper<Boolean> result = controller.existsCompleted(2L, 3L);

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isTrue();
		verify(appointmentService).existsByPatientIdDoctorIdAndStatus(2L, 3L, AppointmentStatus.COMPLETED);
	}

}
