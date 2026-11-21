package com.vention.curae.service.controller;

import com.vention.curae.service.dto.ProvidedServiceCreateDto;
import com.vention.curae.service.dto.ProvidedServiceResponseDto;
import com.vention.curae.service.service.ProvidedServiceService;
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
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ProvidedServiceControllerTest {

	@Mock
	private ProvidedServiceService providedServiceService;

	@InjectMocks
	private ProvidedServiceController providedServiceController;

	@Test
	void getAllReturnsNoContentForEmptyList() {
		Mockito.when(providedServiceService.getAll()).thenReturn(List.of());

		ResponseEntity<List<ProvidedServiceResponseDto>> response = providedServiceController.getAll();

		Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		Assertions.assertNull(response.getBody());
	}

	@Test
	void getAllReturnsOkForNonEmptyList() {
		ProvidedServiceResponseDto dto = Mockito.mock(ProvidedServiceResponseDto.class);
		Mockito.when(providedServiceService.getAll()).thenReturn(List.of(dto));

		ResponseEntity<List<ProvidedServiceResponseDto>> response = providedServiceController.getAll();

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(List.of(dto), response.getBody());
	}

	@Test
	void getByIdReturnsOk() {
		ProvidedServiceResponseDto dto = Mockito.mock(ProvidedServiceResponseDto.class);
		Mockito.when(providedServiceService.getById(1L)).thenReturn(dto);

		ResponseWrapper<ProvidedServiceResponseDto> response = providedServiceController.getById(1L);

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertSame(dto, response.getData());
	}

	@Test
	void createReturnsOk() {
		ProvidedServiceCreateDto dto = Mockito.mock(ProvidedServiceCreateDto.class);
		Mockito.when(providedServiceService.create(dto)).thenReturn(1L);

		ResponseWrapper<Long> response = providedServiceController.create(dto);

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertEquals(Long.valueOf(1L), response.getData());
	}

	@Test
	void deleteDelegatesToService() {
		providedServiceController.delete(1L);

		Mockito.verify(providedServiceService).deleteById(1L);
	}

	@Test
	void updateReturnsOk() {
		ProvidedServiceCreateDto dto = Mockito.mock(ProvidedServiceCreateDto.class);
		Mockito.when(providedServiceService.update(1L, dto)).thenReturn(1L);

		ResponseWrapper<Long> response = providedServiceController.update(1L, dto);

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertEquals(Long.valueOf(1L), response.getData());
	}

	@Test
	void searchReturnsOk() {
		Pageable pageable = Mockito.mock(Pageable.class);
		Mockito.when(providedServiceService.search("query", pageable)).thenReturn(Page.empty());

		ResponseWrapper<Page<ProvidedServiceResponseDto>> response = providedServiceController.search("query",
				pageable);

		Assertions.assertEquals(HttpStatus.OK, response.getStatus());
		Assertions.assertTrue(response.getData().isEmpty());
	}

}
