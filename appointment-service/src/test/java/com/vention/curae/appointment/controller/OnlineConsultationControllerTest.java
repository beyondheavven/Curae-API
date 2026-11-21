package com.vention.curae.appointment.controller;

import com.vention.curae.appointment.dto.CreateOnlineConsultationRequest;
import com.vention.curae.appointment.dto.OnlineConsultationDetails;
import com.vention.curae.appointment.dto.UpdateOnlineConsultationRequest;
import com.vention.curae.appointment.service.OnlineConsultationService;
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
class OnlineConsultationControllerTest {

	@Mock
	private OnlineConsultationService consultationService;

	@InjectMocks
	private OnlineConsultationController controller;

	@Test
	void getDetailsReturnsConsultation() {
		OnlineConsultationDetails details = mock(OnlineConsultationDetails.class);
		when(consultationService.getDetails(1L)).thenReturn(details);

		ResponseWrapper<OnlineConsultationDetails> result = controller.getDetails(1L);

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isSameAs(details);
		verify(consultationService).getDetails(1L);
	}

	@Test
	void createReturnsCreatedStatus() {
		CreateOnlineConsultationRequest dto = mock(CreateOnlineConsultationRequest.class);
		when(consultationService.create(dto)).thenReturn(5L);

		ResponseWrapper<Long> result = controller.create(dto);

		assertThat(result.getStatus()).isEqualTo(HttpStatus.CREATED);
		assertThat(result.getData()).isEqualTo(5L);
		verify(consultationService).create(dto);
	}

	@Test
	void updateDelegatesToService() {
		UpdateOnlineConsultationRequest dto = mock(UpdateOnlineConsultationRequest.class);

		controller.update(dto);

		verify(consultationService).update(dto);
	}

	@Test
	void deleteDelegatesToService() {
		controller.delete(1L);

		verify(consultationService).deleteById(1L);
	}

}
