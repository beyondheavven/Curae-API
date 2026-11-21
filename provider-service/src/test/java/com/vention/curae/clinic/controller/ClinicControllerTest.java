package com.vention.curae.clinic.controller;

import com.vention.curae.clinic.dto.ClinicRequestDto;
import com.vention.curae.clinic.dto.ClinicResponseDto;
import com.vention.curae.clinic.service.ClinicService;
import com.vention.curae.wrapper.ResponseWrapper;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class ClinicControllerTest {

	@Mock
	private ClinicService clinicService;

	@InjectMocks
	private ClinicController clinicController;

	@Test
	void getAllReturnsOk() {
		ClinicResponseDto dto = Mockito.mock(ClinicResponseDto.class);
		Mockito.when(clinicService.getAll()).thenReturn(List.of(dto));

		ResponseWrapper<List<ClinicResponseDto>> response = clinicController.getAll();

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertEquals(List.of(dto), response.getData());
	}

	@Test
	void getByIdReturnsOk() {
		ClinicResponseDto dto = Mockito.mock(ClinicResponseDto.class);
		Mockito.when(clinicService.getById(1L)).thenReturn(dto);

		ResponseWrapper<ClinicResponseDto> response = clinicController.getById(1L);

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertSame(dto, response.getData());
	}

	@Test
	void createReturnsCreated() {
		ClinicRequestDto dto = Mockito.mock(ClinicRequestDto.class);
		Mockito.when(clinicService.create(dto)).thenReturn(1L);

		ResponseWrapper<Long> response = clinicController.create(dto);

		Assertions.assertEquals(HttpStatus.CREATED, response.getStatus());
		Assertions.assertEquals(Long.valueOf(1L), response.getData());
	}

	@Test
	void updateReturnsOk() {
		ClinicRequestDto dto = Mockito.mock(ClinicRequestDto.class);
		Mockito.when(clinicService.update(1L, dto)).thenReturn(1L);

		ResponseWrapper<Long> response = clinicController.update(1L, dto);

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertEquals(Long.valueOf(1L), response.getData());
	}

	@Test
	void deleteDelegatesToService() {
		clinicController.delete(1L);

		Mockito.verify(clinicService).delete(1L);
	}

}
