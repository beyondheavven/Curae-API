package com.vention.curae.specialization.controller;

import com.vention.curae.specialization.dto.SpecializationCreateDto;
import com.vention.curae.specialization.dto.SpecializationResponseDto;
import com.vention.curae.specialization.service.SpecializationService;
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
class SpecializationControllerTest {

	@Mock
	private SpecializationService specializationService;

	@InjectMocks
	private SpecializationController specializationController;

	@Test
	void getAllReturnsOk() {
		SpecializationResponseDto dto = Mockito.mock(SpecializationResponseDto.class);
		Mockito.when(specializationService.getAll()).thenReturn(List.of(dto));

		ResponseWrapper<List<SpecializationResponseDto>> response = specializationController.getAll();

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertEquals(List.of(dto), response.getData());
	}

	@Test
	void getByIdReturnsOk() {
		SpecializationResponseDto dto = Mockito.mock(SpecializationResponseDto.class);
		Mockito.when(specializationService.getById(1L)).thenReturn(dto);

		ResponseWrapper<SpecializationResponseDto> response = specializationController.getById(1L);

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertSame(dto, response.getData());
	}

	@Test
	void createReturnsCreated() {
		SpecializationCreateDto dto = Mockito.mock(SpecializationCreateDto.class);
		Mockito.when(specializationService.create(dto)).thenReturn(1L);

		ResponseWrapper<Long> response = specializationController.create(dto);

		Assertions.assertEquals(HttpStatus.CREATED, response.getStatus());
		Assertions.assertEquals(Long.valueOf(1L), response.getData());
	}

	@Test
	void deleteDelegatesToService() {
		specializationController.delete(1L);

		Mockito.verify(specializationService).deleteById(1L);
	}

	@Test
	void updateReturnsOk() {
		SpecializationCreateDto dto = Mockito.mock(SpecializationCreateDto.class);
		Mockito.when(specializationService.update(1L, dto)).thenReturn(1L);

		ResponseWrapper<Long> response = specializationController.update(1L, dto);

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertEquals(Long.valueOf(1L), response.getData());
	}

}
