package com.vention.curae.user.mapper;

import com.vention.curae.registration.dto.DoctorRegistrationRequestDto;
import com.vention.curae.registration.dto.PersonDto;
import com.vention.curae.user.dto.UserResponseDto;
import com.vention.curae.user.dto.UserUpdateRequestDto;
import com.vention.curae.user.model.RoleType;
import com.vention.curae.user.model.Status;
import com.vention.curae.user.model.User;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserMapperImplTest {

	private final UserMapperImpl mapper = new UserMapperImpl();

	@Test
	void updateUserFromDtoReturnsWhenDtoNull() {
		User user = mock(User.class);
		PasswordEncoder encoder = mock(PasswordEncoder.class);

		mapper.updateUserFromDto(user, null, encoder);
	}

	@Test
	void updateUserFromDtoMapsFieldsAndEncodesPassword() {
		User user = new User();
		PasswordEncoder encoder = mock(PasswordEncoder.class);
		when(encoder.encode("password123")).thenReturn("encoded");
		UserUpdateRequestDto dto = new UserUpdateRequestDto("user@test.com", "password123", "John", "Doe",
				LocalDate.of(2000, 1, 1), Status.ACTIVE, "url");

		mapper.updateUserFromDto(user, dto, encoder);

		assertThat(user.getEmail()).isEqualTo("user@test.com");
		assertThat(user.getFirstName()).isEqualTo("John");
		assertThat(user.getLastName()).isEqualTo("Doe");
		assertThat(user.getProfileUrl()).isEqualTo("url");
		assertThat(user.getBirthDate()).isEqualTo(LocalDate.of(2000, 1, 1));
		assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
		assertThat(user.getPasswordHash()).isEqualTo("encoded");
	}

	@Test
	void updateUserFromDtoSkipsBlankPassword() {
		User user = new User();
		user.setPasswordHash("old");
		PasswordEncoder encoder = mock(PasswordEncoder.class);
		UserUpdateRequestDto dto = new UserUpdateRequestDto("user@test.com", " ", "John", "Doe",
				LocalDate.of(2000, 1, 1), Status.ACTIVE, "url");

		mapper.updateUserFromDto(user, dto, encoder);

		assertThat(user.getPasswordHash()).isEqualTo("old");
	}

	@Test
	void toResponseDtoReturnsNullWhenUserNull() {
		assertThat(mapper.toResponseDto(null)).isNull();
	}

	@Test
	void toResponseDtoMapsFields() {
		User user = mock(User.class);
		when(user.getId()).thenReturn(1L);
		when(user.getEmail()).thenReturn("user@test.com");
		when(user.getFirstName()).thenReturn("John");
		when(user.getLastName()).thenReturn("Doe");
		when(user.getProfileUrl()).thenReturn("url");
		when(user.getBirthDate()).thenReturn(LocalDate.of(2000, 1, 1));
		when(user.getRole()).thenReturn(RoleType.PATIENT);
		when(user.getStatus()).thenReturn(Status.ACTIVE);

		UserResponseDto dto = mapper.toResponseDto(user);

		assertThat(dto.id()).isEqualTo(1L);
		assertThat(dto.email()).isEqualTo("user@test.com");
		assertThat(dto.firstName()).isEqualTo("John");
		assertThat(dto.lastName()).isEqualTo("Doe");
		assertThat(dto.profileUrl()).isEqualTo("url");
		assertThat(dto.birthDate()).isEqualTo(LocalDate.of(2000, 1, 1));
		assertThat(dto.role()).isEqualTo(RoleType.PATIENT);
		assertThat(dto.status()).isEqualTo(Status.ACTIVE);
	}

	@Test
	void toPatientUserReturnsNullWhenDtoNull() {
		assertThat(mapper.toPatientUser(null, mock(PasswordEncoder.class))).isNull();
	}

	@Test
	void toPatientUserMapsFieldsAndEncodesPassword() {
		PersonDto personDto = mock(PersonDto.class);
		when(personDto.getName()).thenReturn("John");
		when(personDto.getBirthDate()).thenReturn(LocalDate.of(2000, 1, 1));
		when(personDto.getEmail()).thenReturn("user@test.com");
		when(personDto.getLastName()).thenReturn("Doe");
		when(personDto.getRawPassword()).thenReturn("raw-pass");
		PasswordEncoder encoder = mock(PasswordEncoder.class);
		when(encoder.encode("raw-pass")).thenReturn("encoded");

		User user = mapper.toPatientUser(personDto, encoder);

		assertThat(user.getFirstName()).isEqualTo("John");
		assertThat(user.getBirthDate()).isEqualTo(LocalDate.of(2000, 1, 1));
		assertThat(user.getEmail()).isEqualTo("user@test.com");
		assertThat(user.getLastName()).isEqualTo("Doe");
		assertThat(user.getRole()).isEqualTo(RoleType.PATIENT);
		assertThat(user.getStatus()).isEqualTo(Status.UNVERIFIED);
		assertThat(user.getPasswordHash()).isEqualTo("encoded");
	}

	@Test
	void toDoctorUserReturnsNullWhenBothNull() {
		assertThat(mapper.toDoctorUser(null, null, mock(PasswordEncoder.class))).isNull();
	}

	@Test
	void toDoctorUserMapsFieldsAndEncodesPassword() {
		DoctorRegistrationRequestDto dto = new DoctorRegistrationRequestDto("John", "Doe", LocalDate.of(1980, 1, 1),
				Set.of(1L), "LIC123", "doc@test.com", "+123456789");
		PasswordEncoder encoder = mock(PasswordEncoder.class);
		when(encoder.encode("temp")).thenReturn("encoded-temp");

		User user = mapper.toDoctorUser(dto, "temp", encoder);

		assertThat(user.getEmail()).isEqualTo("doc@test.com");
		assertThat(user.getFirstName()).isEqualTo("John");
		assertThat(user.getLastName()).isEqualTo("Doe");
		assertThat(user.getBirthDate()).isEqualTo(LocalDate.of(1980, 1, 1));
		assertThat(user.getStatus()).isEqualTo(Status.UNVERIFIED);
		assertThat(user.getRole()).isEqualTo(RoleType.DOCTOR);
		assertThat(user.getPasswordHash()).isEqualTo("encoded-temp");
		verify(encoder).encode("temp");
	}

}
