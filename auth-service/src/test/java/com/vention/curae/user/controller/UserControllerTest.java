package com.vention.curae.user.controller;

import com.vention.curae.user.dto.UserResponseDto;
import com.vention.curae.user.dto.UserUpdateRequestDto;
import com.vention.curae.user.model.RoleType;
import com.vention.curae.user.model.Status;
import com.vention.curae.user.service.UserService;
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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	@Test
	void getByIdReturnsUser() {
		UserResponseDto dto = new UserResponseDto(1L, "user@test.com", "John", "Doe", "url", LocalDate.of(2000, 1, 1),
				RoleType.PATIENT, Status.ACTIVE);
		when(userService.getById(1L)).thenReturn(dto);

		ResponseWrapper<UserResponseDto> result = userController.getById(1L);

		assertThat(result.getData()).isEqualTo(dto);
		verify(userService).getById(1L);
	}

	@Test
	void getAllReturnsPage() {
		Pageable pageable = PageRequest.of(0, 20);
		UserResponseDto dto = new UserResponseDto(1L, "user@test.com", "John", "Doe", "url", LocalDate.of(2000, 1, 1),
				RoleType.PATIENT, Status.ACTIVE);
		Page<UserResponseDto> page = new PageImpl<>(List.of(dto), pageable, 1);
		when(userService.getAll(pageable)).thenReturn(page);

		ResponseWrapper<Page<UserResponseDto>> result = userController.getAll(pageable);

		assertThat(result.getData().getContent()).containsExactly(dto);
		verify(userService).getAll(pageable);
	}

	@Test
	void updateDelegatesToService() {
		UserUpdateRequestDto request = mock(UserUpdateRequestDto.class);
		UserResponseDto dto = new UserResponseDto(1L, "user@test.com", "John", "Doe", "url", LocalDate.of(2000, 1, 1),
				RoleType.PATIENT, Status.ACTIVE);
		when(userService.updateUser(1L, request)).thenReturn(dto);

		ResponseWrapper<UserResponseDto> result = userController.update(1L, request);

		assertThat(result.getData()).isEqualTo(dto);
		verify(userService).updateUser(1L, request);
	}

}
