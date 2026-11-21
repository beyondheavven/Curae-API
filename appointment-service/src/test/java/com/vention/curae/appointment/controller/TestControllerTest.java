package com.vention.curae.appointment.controller;

import com.vention.curae.appointment.dto.PatientLabHistoryDTO;
import com.vention.curae.appointment.dto.TestCreateWithLabsDto;
import com.vention.curae.appointment.security.AppointmentSecurityService;
import com.vention.curae.appointment.service.TestService;
import com.vention.curae.wrapper.ResponseWrapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestControllerTest {

	@Mock
	private TestService testService;

	@Mock
	private AppointmentSecurityService securityService;

	@InjectMocks
	private TestController controller;

	@Test
	void createDelegatesToService() {
		TestCreateWithLabsDto dto = mock(TestCreateWithLabsDto.class);

		controller.create(dto);

		verify(testService).createWithLabs(dto);
	}

	@Test
	void getHistoryReturnsPatientLabHistory() {
		Pageable pageable = PageRequest.of(0, 20);
		Page<PatientLabHistoryDTO> page = new PageImpl<>(List.of(mock(PatientLabHistoryDTO.class)));
		when(securityService.currentPatientId()).thenReturn(7L);
		when(testService.getPatientLabHistory(7L, pageable)).thenReturn(page);

		ResponseWrapper<Page<PatientLabHistoryDTO>> result = controller.getHistory(pageable);

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isSameAs(page);
		verify(securityService).currentPatientId();
		verify(testService).getPatientLabHistory(7L, pageable);
	}

}
