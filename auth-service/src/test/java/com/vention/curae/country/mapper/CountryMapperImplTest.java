package com.vention.curae.country.mapper;

import com.vention.curae.country.dto.CountryResponseDto;
import com.vention.curae.country.model.Country;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CountryMapperImplTest {

	private final CountryMapperImpl mapper = new CountryMapperImpl();

	@Test
	void toDtoReturnsNullWhenEntityNull() {
		assertThat(mapper.toDto(null)).isNull();
	}

	@Test
	void toDtoMapsFields() {
		Country country = mock(Country.class);
		when(country.getId()).thenReturn(1L);
		when(country.getIsoCode()).thenReturn("US");
		when(country.getName()).thenReturn("United States");
		when(country.getCitizenship()).thenReturn("American");
		when(country.getZipRegex()).thenReturn("\\d+");
		when(country.getZipExample()).thenReturn("12345");

		CountryResponseDto dto = mapper.toDto(country);

		assertThat(dto.id()).isEqualTo(1L);
		assertThat(dto.isoCode()).isEqualTo("US");
		assertThat(dto.name()).isEqualTo("United States");
		assertThat(dto.citizenship()).isEqualTo("American");
		assertThat(dto.zipRegex()).isEqualTo("\\d+");
		assertThat(dto.zipExample()).isEqualTo("12345");
	}

}
