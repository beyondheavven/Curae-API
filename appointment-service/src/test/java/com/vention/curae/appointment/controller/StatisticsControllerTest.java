package com.vention.curae.appointment.controller;

import com.vention.curae.appointment.dto.StatusStatisticDto;
import com.vention.curae.appointment.service.StatisticsService;
import com.vention.curae.wrapper.ResponseWrapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticsControllerTest {

	@Mock
	private StatisticsService statisticsService;

	@InjectMocks
	private StatisticsController controller;

	@Test
	void appointmentsByStatusReturnsStatistics() {
		List<StatusStatisticDto> statistics = List.of(new StatusStatisticDto("NEW", 3L));
		when(statisticsService.appointmentStatusStatistics()).thenReturn(statistics);

		ResponseWrapper<List<StatusStatisticDto>> result = controller.appointmentsByStatus();

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isSameAs(statistics);
		verify(statisticsService).appointmentStatusStatistics();
	}

	@Test
	void visitsByClinicReturnsStatistics() {
		List<StatusStatisticDto> statistics = List.of(new StatusStatisticDto("CLINIC", 2L));
		when(statisticsService.visitStatisticsByClinic()).thenReturn(statistics);

		ResponseWrapper<List<StatusStatisticDto>> result = controller.visitsByClinic();

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isSameAs(statistics);
		verify(statisticsService).visitStatisticsByClinic();
	}

	@Test
	void visitsByDoctorReturnsStatistics() {
		List<StatusStatisticDto> statistics = List.of(new StatusStatisticDto("DOCTOR", 1L));
		when(statisticsService.visitStatisticsByDoctor()).thenReturn(statistics);

		ResponseWrapper<List<StatusStatisticDto>> result = controller.visitsByDoctor();

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isSameAs(statistics);
		verify(statisticsService).visitStatisticsByDoctor();
	}

	@Test
	void testsByClinicReturnsStatistics() {
		List<StatusStatisticDto> statistics = List.of(new StatusStatisticDto("CLINIC", 4L));
		when(statisticsService.testStatisticsByClinic()).thenReturn(statistics);

		ResponseWrapper<List<StatusStatisticDto>> result = controller.testsByClinic();

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isSameAs(statistics);
		verify(statisticsService).testStatisticsByClinic();
	}

}
