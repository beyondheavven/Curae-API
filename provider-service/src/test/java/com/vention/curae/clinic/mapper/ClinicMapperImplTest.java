package com.vention.curae.clinic.mapper;

import com.vention.curae.address.dto.ClinicAddressRequestDto;
import com.vention.curae.address.dto.ClinicAddressResponseDto;
import com.vention.curae.address.model.Address;
import com.vention.curae.clinic.dto.ClinicRequestDto;
import com.vention.curae.clinic.dto.ClinicResponseDto;
import com.vention.curae.clinic.model.Clinic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClinicMapperImplTest {

	private final ClinicMapperImpl mapper = new ClinicMapperImpl();

	@Test
	void toEntityHandlesNullAndFields() {
		Assertions.assertNull(mapper.toEntity(null));

		ClinicRequestDto dto = Mockito.mock(ClinicRequestDto.class);
		Mockito.when(dto.name()).thenReturn("Clinic");
		Mockito.when(dto.logoUrl()).thenReturn("logo");
		Mockito.when(dto.description()).thenReturn("description");

		Clinic clinic = mapper.toEntity(dto);

		Assertions.assertEquals("Clinic", clinic.getName());
		Assertions.assertEquals("logo", clinic.getLogoUrl());
		Assertions.assertEquals("description", clinic.getDescription());
	}

	@Test
	void toEntityWithAddressHandlesNullAndFields() {
		Assertions.assertNull(mapper.toEntityWithAddress(null));

		ClinicAddressRequestDto addressDto = Mockito.mock(ClinicAddressRequestDto.class);
		ClinicRequestDto dto = Mockito.mock(ClinicRequestDto.class);
		Mockito.when(dto.address()).thenReturn(addressDto);
		Mockito.when(dto.name()).thenReturn("Clinic");
		Mockito.when(dto.logoUrl()).thenReturn("logo");
		Mockito.when(dto.description()).thenReturn("description");

		Clinic clinic = mapper.toEntityWithAddress(dto);

		Assertions.assertNotNull(clinic.getAddress());
		Assertions.assertEquals("Clinic", clinic.getName());
		Assertions.assertEquals("logo", clinic.getLogoUrl());
		Assertions.assertEquals("description", clinic.getDescription());
	}

	@Test
	void updateEntityHandlesNullAndFields() {
		Clinic emptyTarget = Mockito.mock(Clinic.class);
		mapper.updateEntity(null, emptyTarget);
		Mockito.verifyNoInteractions(emptyTarget);

		Clinic clinic = Mockito.mock(Clinic.class);
		ClinicRequestDto dto = Mockito.mock(ClinicRequestDto.class);
		Mockito.when(dto.name()).thenReturn("Clinic");
		Mockito.when(dto.logoUrl()).thenReturn("logo");
		Mockito.when(dto.description()).thenReturn("description");

		mapper.updateEntity(dto, clinic);

		Mockito.verify(clinic).setName("Clinic");
		Mockito.verify(clinic).setLogoUrl("logo");
		Mockito.verify(clinic).setDescription("description");

		ClinicRequestDto emptyDto = Mockito.mock(ClinicRequestDto.class);
		mapper.updateEntity(emptyDto, clinic);
		Mockito.verifyNoMoreInteractions(clinic);
	}

	@Test
	void toAddressHandlesNullAndFields() {
		Assertions.assertNull(mapper.toAddress(null));

		ClinicAddressRequestDto dto = Mockito.mock(ClinicAddressRequestDto.class);
		Mockito.when(dto.street()).thenReturn("Street");
		Mockito.when(dto.city()).thenReturn("City");
		Mockito.when(dto.houseNumber()).thenReturn("1");
		Mockito.when(dto.state()).thenReturn("State");
		Mockito.when(dto.apartment()).thenReturn("2");
		Mockito.when(dto.zipCode()).thenReturn("00000");
		Mockito.when(dto.phone()).thenReturn("123");

		Address address = mapper.toAddress(dto);

		Assertions.assertEquals("Street", address.getStreet());
		Assertions.assertEquals("City", address.getCity());
		Assertions.assertEquals("1", address.getHouseNumber());
		Assertions.assertEquals("State", address.getState());
		Assertions.assertEquals("2", address.getApartment());
		Assertions.assertEquals("00000", address.getZipCode());
		Assertions.assertEquals("123", address.getPhone());
	}

	@Test
	void updateAddressHandlesNullAndFields() {
		Address emptyTarget = Mockito.mock(Address.class);
		mapper.updateAddress(null, emptyTarget);
		Mockito.verifyNoInteractions(emptyTarget);

		ClinicAddressRequestDto dto = Mockito.mock(ClinicAddressRequestDto.class);
		Mockito.when(dto.street()).thenReturn("Street");
		Mockito.when(dto.city()).thenReturn("City");
		Mockito.when(dto.houseNumber()).thenReturn("1");
		Mockito.when(dto.state()).thenReturn("State");
		Mockito.when(dto.apartment()).thenReturn("2");
		Mockito.when(dto.zipCode()).thenReturn("00000");
		Mockito.when(dto.phone()).thenReturn("123");
		Address address = Mockito.mock(Address.class);

		mapper.updateAddress(dto, address);

		Mockito.verify(address).setStreet("Street");
		Mockito.verify(address).setCity("City");
		Mockito.verify(address).setHouseNumber("1");
		Mockito.verify(address).setState("State");
		Mockito.verify(address).setApartment("2");
		Mockito.verify(address).setZipCode("00000");
		Mockito.verify(address).setPhone("123");
	}

	@Test
	void toResponseDtoHandlesNullAndFields() {
		Assertions.assertNull(mapper.toResponseDto(null));

		Clinic clinic = Mockito.mock(Clinic.class);
		Mockito.when(clinic.getId()).thenReturn(1L);
		Mockito.when(clinic.getName()).thenReturn("Clinic");
		Mockito.when(clinic.getLogoUrl()).thenReturn("logo");
		Mockito.when(clinic.getDescription()).thenReturn("description");
		Mockito.when(clinic.getAddress()).thenReturn(Mockito.mock(Address.class));

		ClinicResponseDto dto = mapper.toResponseDto(clinic);

		Assertions.assertEquals(Long.valueOf(1L), dto.id());
		Assertions.assertEquals("Clinic", dto.name());
		Assertions.assertEquals("logo", dto.logoUrl());
		Assertions.assertEquals("description", dto.description());
		Assertions.assertNotNull(dto.address());
	}

	@Test
	void toAddressResponseDtoHandlesNullAndFields() {
		Assertions.assertNull(mapper.toAddressResponseDto(null));

		Address address = Mockito.mock(Address.class);
		Mockito.when(address.getId()).thenReturn(1L);
		Mockito.when(address.getStreet()).thenReturn("Street");
		Mockito.when(address.getCity()).thenReturn("City");
		Mockito.when(address.getHouseNumber()).thenReturn("1");
		Mockito.when(address.getState()).thenReturn("State");
		Mockito.when(address.getApartment()).thenReturn("2");
		Mockito.when(address.getZipCode()).thenReturn("00000");
		Mockito.when(address.getPhone()).thenReturn("123");

		ClinicAddressResponseDto dto = mapper.toAddressResponseDto(address);

		Assertions.assertEquals(Long.valueOf(1L), dto.id());
		Assertions.assertEquals("Street", dto.street());
		Assertions.assertEquals("City", dto.city());
		Assertions.assertEquals("1", dto.houseNumber());
		Assertions.assertEquals("State", dto.state());
		Assertions.assertEquals("2", dto.apartment());
		Assertions.assertEquals("00000", dto.zipCode());
		Assertions.assertEquals("123", dto.phone());
	}

}
