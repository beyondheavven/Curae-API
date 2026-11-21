package com.vention.curae.doctor.controller;

import com.vention.curae.doctor.dto.DoctorClinicAssignmentRequestDto;
import com.vention.curae.doctor.dto.DoctorResponseDto;
import com.vention.curae.doctor.dto.DoctorSearchDto;
import com.vention.curae.doctor.dto.DoctorSearchResponseDto;
import com.vention.curae.doctor.dto.DoctorUpdateDto;
import com.vention.curae.doctor.service.DoctorService;
import com.vention.curae.wrapper.ResponseWrapper;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {

	@Mock
	private DoctorService doctorService;

	@InjectMocks
	private DoctorController doctorController;

	@Test
	void searchReturnsOk() {
		DoctorSearchDto searchDto = Mockito.mock(DoctorSearchDto.class);
		Pageable pageable = Mockito.mock(Pageable.class);
		Mockito.when(doctorService.search(searchDto, pageable)).thenReturn(Page.empty());

		ResponseWrapper<Page<DoctorSearchResponseDto>> response = doctorController.search(searchDto, pageable);

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertTrue(response.getData().isEmpty());
	}

	@Test
	void setClinicsReturnsOk() {
		DoctorClinicAssignmentRequestDto dto = Mockito.mock(DoctorClinicAssignmentRequestDto.class);
		DoctorResponseDto responseDto = Mockito.mock(DoctorResponseDto.class);
		Mockito.when(doctorService.setClinics(1L, dto)).thenReturn(responseDto);

		ResponseWrapper<DoctorResponseDto> response = doctorController.setClinics(1L, dto);

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertSame(responseDto, response.getData());
	}

	@Test
	void getByIdReturnsOk() {
		DoctorResponseDto dto = Mockito.mock(DoctorResponseDto.class);
		Mockito.when(doctorService.getById(1L)).thenReturn(dto);

		ResponseWrapper<DoctorResponseDto> response = doctorController.getById(1L);

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertSame(dto, response.getData());
	}

	@Test
	void getAllReturnsOk() {
		DoctorResponseDto dto = Mockito.mock(DoctorResponseDto.class);
		Mockito.when(doctorService.getAll()).thenReturn(List.of(dto));

		ResponseWrapper<List<DoctorResponseDto>> response = doctorController.getAll();

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertEquals(List.of(dto), response.getData());
	}

	@Test
	void updateReturnsOk() {
		DoctorUpdateDto dto = Mockito.mock(DoctorUpdateDto.class);
		DoctorResponseDto responseDto = Mockito.mock(DoctorResponseDto.class);
		Mockito.when(doctorService.update(1L, dto)).thenReturn(responseDto);

		ResponseWrapper<DoctorResponseDto> response = doctorController.update(1L, dto);

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertSame(responseDto, response.getData());
	}

	@Test
	void deleteDelegatesToService() {
		doctorController.delete(1L);

		Mockito.verify(doctorService).delete(1L);
	}

}
