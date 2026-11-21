package com.vention.curae.country.controller;

import com.vention.curae.country.dto.CountryResponseDto;
import com.vention.curae.country.service.CountryService;
import com.vention.curae.wrapper.ResponseWrapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryControllerTest {

	@Mock
	private CountryService countryService;

	@InjectMocks
	private CountryController countryController;

	@Test
	void getAllCountriesReturnsPage() {
		Pageable pageable = PageRequest.of(0, 20);
		CountryResponseDto dto = new CountryResponseDto(1L, "US", "United States", "American", "\\d+", "12345");
		Page<CountryResponseDto> page = new PageImpl<>(List.of(dto), pageable, 1);
		when(countryService.getAll(pageable)).thenReturn(page);

		ResponseWrapper<Page<CountryResponseDto>> result = countryController.getAllCountries(pageable);

		assertThat(result.getData().getContent()).containsExactly(dto);
		verify(countryService).getAll(pageable);
	}

}
