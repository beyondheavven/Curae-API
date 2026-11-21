package com.vention.curae.specialization.mapper;

import com.vention.curae.service.dto.ProvidedServiceBaseDto;
import com.vention.curae.service.model.ProvidedService;
import com.vention.curae.specialization.dto.SpecializationBaseDto;
import com.vention.curae.specialization.dto.SpecializationCreateDto;
import com.vention.curae.specialization.dto.SpecializationResponseDto;
import com.vention.curae.specialization.model.Specialization;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpecializationMapperImplTest {

	private final SpecializationMapperImpl mapper = new SpecializationMapperImpl();

	@Test
	void toEntityHandlesNullAndFields() {
		Assertions.assertNull(mapper.toEntity(null));

		SpecializationCreateDto dto = Mockito.mock(SpecializationCreateDto.class);
		Mockito.when(dto.name()).thenReturn("Specialization");
		Mockito.when(dto.description()).thenReturn("Description");

		Specialization entity = mapper.toEntity(dto);

		Assertions.assertEquals("Specialization", entity.getName());
		Assertions.assertEquals("Description", entity.getDescription());
	}

	@Test
	void toDtoHandlesNullAndFields() {
		Assertions.assertNull(mapper.toDto(null));

		ProvidedService service = Mockito.mock(ProvidedService.class);
		Mockito.when(service.getId()).thenReturn(2L);
		Mockito.when(service.getName()).thenReturn("Service");
		Mockito.when(service.getDescription()).thenReturn("Service description");
		Mockito.when(service.getPrice()).thenReturn(BigDecimal.valueOf(12.5));
		Mockito.when(service.getDuration()).thenReturn(30);
		Specialization entity = Mockito.mock(Specialization.class);
		Mockito.when(entity.getId()).thenReturn(1L);
		Mockito.when(entity.getName()).thenReturn("Specialization");
		Mockito.when(entity.getDescription()).thenReturn("Description");
		Mockito.when(entity.getServices()).thenReturn(List.of(service));

		SpecializationResponseDto dto = mapper.toDto(entity);

		Assertions.assertEquals(Long.valueOf(1L), dto.id());
		Assertions.assertEquals("Specialization", dto.name());
		Assertions.assertEquals("Description", dto.description());
		Assertions.assertEquals(1, dto.services().size());
		Assertions.assertEquals(Double.valueOf(12.5), dto.services().get(0).price());

		Specialization entityWithoutServices = Mockito.mock(Specialization.class);
		Mockito.when(entityWithoutServices.getServices()).thenReturn(null);

		Assertions.assertNull(mapper.toDto(entityWithoutServices).services());
	}

	@Test
	void toBaseDtoHandlesNullAndFields() {
		Assertions.assertNull(mapper.toBaseDto(null));

		Specialization entity = Mockito.mock(Specialization.class);
		Mockito.when(entity.getId()).thenReturn(1L);
		Mockito.when(entity.getName()).thenReturn("Specialization");
		Mockito.when(entity.getDescription()).thenReturn("Description");

		SpecializationBaseDto dto = mapper.toBaseDto(entity);

		Assertions.assertEquals(Long.valueOf(1L), dto.id());
		Assertions.assertEquals("Specialization", dto.name());
		Assertions.assertEquals("Description", dto.description());
	}

	@Test
	void toServiceBaseDtoHandlesNullAndFields() {
		Assertions.assertNull(mapper.toServiceBaseDto(null));

		ProvidedService entity = Mockito.mock(ProvidedService.class);
		Mockito.when(entity.getId()).thenReturn(2L);
		Mockito.when(entity.getName()).thenReturn("Service");
		Mockito.when(entity.getDescription()).thenReturn("Service description");
		Mockito.when(entity.getDuration()).thenReturn(30);
		Mockito.when(entity.getPrice()).thenReturn(BigDecimal.valueOf(12.5));

		ProvidedServiceBaseDto dto = mapper.toServiceBaseDto(entity);

		Assertions.assertEquals(Long.valueOf(2L), dto.id());
		Assertions.assertEquals("Service", dto.name());
		Assertions.assertEquals("Service description", dto.description());
		Assertions.assertEquals(Double.valueOf(12.5), dto.price());
		Assertions.assertEquals(Integer.valueOf(30), dto.duration());

		ProvidedService entityWithoutPrice = Mockito.mock(ProvidedService.class);

		Assertions.assertNull(mapper.toServiceBaseDto(entityWithoutPrice).price());
	}

	@Test
	void updateFromDtoHandlesNullAndFields() {
		Specialization emptyTarget = Mockito.mock(Specialization.class);
		mapper.updateFromDto(null, emptyTarget);
		Mockito.verifyNoInteractions(emptyTarget);

		SpecializationCreateDto dto = Mockito.mock(SpecializationCreateDto.class);
		Mockito.when(dto.name()).thenReturn("Specialization");
		Mockito.when(dto.description()).thenReturn("Description");
		Specialization entity = Mockito.mock(Specialization.class);

		mapper.updateFromDto(dto, entity);

		Mockito.verify(entity).setName("Specialization");
		Mockito.verify(entity).setDescription("Description");

		SpecializationCreateDto emptyDto = Mockito.mock(SpecializationCreateDto.class);
		Specialization otherEntity = Mockito.mock(Specialization.class);

		mapper.updateFromDto(emptyDto, otherEntity);

		Mockito.verify(otherEntity, Mockito.never()).setName(Mockito.any());
		Mockito.verify(otherEntity, Mockito.never()).setDescription(Mockito.any());
	}

	@Test
	void providedServiceListToProvidedServiceBaseDtoListHandlesNullAndElements() {
		Assertions.assertNull(mapper.providedServiceListToProvidedServiceBaseDtoList(null));

		ProvidedService service = Mockito.mock(ProvidedService.class);
		Mockito.when(service.getName()).thenReturn("Service");

		List<ProvidedServiceBaseDto> result = mapper.providedServiceListToProvidedServiceBaseDtoList(List.of(service));

		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals("Service", result.get(0).name());
	}

	@Test
	void mapHandlesNullAndValue() {
		Assertions.assertNull(mapper.map(null));
		Assertions.assertEquals(Double.valueOf(12.5), mapper.map(BigDecimal.valueOf(12.5)));
	}

}
