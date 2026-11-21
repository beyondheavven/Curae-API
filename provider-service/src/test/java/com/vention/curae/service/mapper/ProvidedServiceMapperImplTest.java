package com.vention.curae.service.mapper;

import com.vention.curae.service.dto.ProvidedServiceBaseDto;
import com.vention.curae.service.dto.ProvidedServiceCreateDto;
import com.vention.curae.service.dto.ProvidedServiceResponseDto;
import com.vention.curae.service.model.ProvidedService;
import com.vention.curae.specialization.dto.SpecializationBaseDto;
import com.vention.curae.specialization.model.Specialization;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProvidedServiceMapperImplTest {

	private final ProvidedServiceMapperImpl mapper = new ProvidedServiceMapperImpl();

	@Test
	void toEntityHandlesNullAndFields() {
		Assertions.assertNull(mapper.toEntity(null));

		ProvidedServiceCreateDto dto = Mockito.mock(ProvidedServiceCreateDto.class);
		Mockito.when(dto.name()).thenReturn("Service");
		Mockito.when(dto.description()).thenReturn("Description");
		Mockito.when(dto.duration()).thenReturn(30);
		Mockito.when(dto.price()).thenReturn(12.5);

		ProvidedService entity = mapper.toEntity(dto);

		Assertions.assertEquals("Service", entity.getName());
		Assertions.assertEquals("Description", entity.getDescription());
		Assertions.assertEquals(Integer.valueOf(30), entity.getDuration());
		Assertions.assertEquals(BigDecimal.valueOf(12.5), entity.getPrice());

		ProvidedServiceCreateDto nullPriceDto = Mockito.mock(ProvidedServiceCreateDto.class);
		Mockito.when(nullPriceDto.price()).thenReturn(null);

		Assertions.assertNull(mapper.toEntity(nullPriceDto).getPrice());
	}

	@Test
	void toDtoHandlesNullAndFields() {
		Assertions.assertNull(mapper.toDto(null));

		Specialization specialization = Mockito.mock(Specialization.class);
		Mockito.when(specialization.getId()).thenReturn(2L);
		Mockito.when(specialization.getName()).thenReturn("Specialization");
		Mockito.when(specialization.getDescription()).thenReturn("Specialization description");
		ProvidedService entity = Mockito.mock(ProvidedService.class);
		Mockito.when(entity.getId()).thenReturn(1L);
		Mockito.when(entity.getName()).thenReturn("Service");
		Mockito.when(entity.getDescription()).thenReturn("Description");
		Mockito.when(entity.getDuration()).thenReturn(30);
		Mockito.when(entity.getPrice()).thenReturn(BigDecimal.valueOf(12.5));
		Mockito.when(entity.getSpecializations()).thenReturn(List.of(specialization));

		ProvidedServiceResponseDto dto = mapper.toDto(entity);

		Assertions.assertEquals(Long.valueOf(1L), dto.id());
		Assertions.assertEquals("Service", dto.name());
		Assertions.assertEquals("Description", dto.description());
		Assertions.assertEquals(Double.valueOf(12.5), dto.price());
		Assertions.assertEquals(Integer.valueOf(30), dto.duration());
		Assertions.assertEquals(1, dto.specializations().size());

		ProvidedService entityWithoutPrice = Mockito.mock(ProvidedService.class);
		Mockito.when(entityWithoutPrice.getSpecializations()).thenReturn(null);

		ProvidedServiceResponseDto dtoWithoutPrice = mapper.toDto(entityWithoutPrice);

		Assertions.assertNull(dtoWithoutPrice.price());
		Assertions.assertNull(dtoWithoutPrice.specializations());
	}

	@Test
	void toSpecializationBaseDtoHandlesNullAndFields() {
		Assertions.assertNull(mapper.toSpecializationBaseDto(null));

		Specialization entity = Mockito.mock(Specialization.class);
		Mockito.when(entity.getId()).thenReturn(2L);
		Mockito.when(entity.getName()).thenReturn("Specialization");
		Mockito.when(entity.getDescription()).thenReturn("Specialization description");

		SpecializationBaseDto dto = mapper.toSpecializationBaseDto(entity);

		Assertions.assertEquals(Long.valueOf(2L), dto.id());
		Assertions.assertEquals("Specialization", dto.name());
		Assertions.assertEquals("Specialization description", dto.description());
	}

	@Test
	void toBaseDtoHandlesNullAndFields() {
		Assertions.assertNull(mapper.toBaseDto(null));

		ProvidedService entity = Mockito.mock(ProvidedService.class);
		Mockito.when(entity.getId()).thenReturn(1L);
		Mockito.when(entity.getName()).thenReturn("Service");
		Mockito.when(entity.getDescription()).thenReturn("Description");
		Mockito.when(entity.getDuration()).thenReturn(30);
		Mockito.when(entity.getPrice()).thenReturn(BigDecimal.valueOf(12.5));

		ProvidedServiceBaseDto dto = mapper.toBaseDto(entity);

		Assertions.assertEquals(Long.valueOf(1L), dto.id());
		Assertions.assertEquals("Service", dto.name());
		Assertions.assertEquals("Description", dto.description());
		Assertions.assertEquals(Double.valueOf(12.5), dto.price());
		Assertions.assertEquals(Integer.valueOf(30), dto.duration());

		ProvidedService entityWithoutPrice = Mockito.mock(ProvidedService.class);

		Assertions.assertNull(mapper.toBaseDto(entityWithoutPrice).price());
	}

	@Test
	void updateFromDtoHandlesNullAndFields() {
		ProvidedService emptyTarget = Mockito.mock(ProvidedService.class);
		mapper.updateFromDto(null, emptyTarget);
		Mockito.verifyNoInteractions(emptyTarget);

		ProvidedServiceCreateDto dto = Mockito.mock(ProvidedServiceCreateDto.class);
		Mockito.when(dto.name()).thenReturn("Service");
		Mockito.when(dto.description()).thenReturn("Description");
		Mockito.when(dto.duration()).thenReturn(30);
		Mockito.when(dto.price()).thenReturn(12.5);
		ProvidedService entity = Mockito.mock(ProvidedService.class);

		mapper.updateFromDto(dto, entity);

		Mockito.verify(entity).setName("Service");
		Mockito.verify(entity).setDescription("Description");
		Mockito.verify(entity).setDuration(30);
		Mockito.verify(entity).setPrice(BigDecimal.valueOf(12.5));

		ProvidedServiceCreateDto nullPriceDto = Mockito.mock(ProvidedServiceCreateDto.class);
		Mockito.when(nullPriceDto.price()).thenReturn(null);
		Mockito.when(nullPriceDto.duration()).thenReturn(null);
		ProvidedService entityWithoutPrice = Mockito.mock(ProvidedService.class);

		mapper.updateFromDto(nullPriceDto, entityWithoutPrice);

		Mockito.verify(entityWithoutPrice).setPrice(null);
	}

	@Test
	void toDtoListHandlesNullAndElements() {
		Assertions.assertNull(mapper.toDtoList(null));
		Assertions.assertTrue(mapper.toDtoList(List.of()).isEmpty());

		ProvidedService first = Mockito.mock(ProvidedService.class);
		Mockito.when(first.getPrice()).thenReturn(BigDecimal.valueOf(12.5));
		ProvidedService second = Mockito.mock(ProvidedService.class);

		List<ProvidedServiceBaseDto> result = mapper.toDtoList(List.of(first, second));

		Assertions.assertEquals(2, result.size());
		Assertions.assertEquals(Double.valueOf(12.5), result.get(0).price());
		Assertions.assertNull(result.get(1).price());
	}

	@Test
	void specializationListToSpecializationBaseDtoListHandlesNullAndElements() {
		Assertions.assertNull(mapper.specializationListToSpecializationBaseDtoList(null));

		Specialization specialization = Mockito.mock(Specialization.class);
		Mockito.when(specialization.getName()).thenReturn("Specialization");

		List<SpecializationBaseDto> result = mapper
			.specializationListToSpecializationBaseDtoList(List.of(specialization));

		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals("Specialization", result.get(0).name());
	}

}
