package com.vention.curae.notification.controller;

import com.vention.curae.notification.dto.NotificationDetailsDto;
import com.vention.curae.notification.dto.NotificationDto;
import com.vention.curae.notification.service.NotificationsService;
import com.vention.curae.wrapper.ResponseWrapper;
import java.security.Principal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

	@Mock
	private NotificationsService notificationService;

	@InjectMocks
	private NotificationController controller;

	@Test
	void getNotificationsUsesHeaderPatientId() {
		Pageable pageable = PageRequest.of(0, 20);
		Page<NotificationDto> page = Page.empty();
		when(notificationService.getPatientNotifications(5L, pageable)).thenReturn(page);

		ResponseWrapper<Page<NotificationDto>> result = controller.getNotifications(5L, null, pageable);

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isSameAs(page);
		verify(notificationService).getPatientNotifications(5L, pageable);
	}

	@Test
	void getNotificationsUsesPrincipalWhenHeaderIsNull() {
		Pageable pageable = PageRequest.of(0, 20);
		Principal principal = mock(Principal.class);
		Page<NotificationDto> page = Page.empty();
		when(principal.getName()).thenReturn("1");
		when(notificationService.getPatientNotifications(1L, pageable)).thenReturn(page);

		ResponseWrapper<Page<NotificationDto>> result = controller.getNotifications(null, principal, pageable);

		assertThat(result.getData()).isSameAs(page);
		verify(notificationService).getPatientNotifications(1L, pageable);
	}

	@Test
	void getNotificationDetailsReturnsDetails() {
		NotificationDetailsDto details = mock(NotificationDetailsDto.class);
		when(notificationService.getNotificationDetails(3L)).thenReturn(details);

		ResponseWrapper<NotificationDetailsDto> result = controller.getNotificationDetails(3L);

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isSameAs(details);
		verify(notificationService).getNotificationDetails(3L);
	}

	@Test
	void markNotificationAsReadUsesHeaderPatientId() {
		controller.markNotificationAsRead(3L, 5L, null);

		verify(notificationService).markAsRead(3L, 5L);
	}

	@Test
	void markNotificationAsReadUsesPrincipalWhenHeaderIsNull() {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("1");

		controller.markNotificationAsRead(3L, null, principal);

		verify(notificationService).markAsRead(3L, 1L);
	}

	@Test
	void resolvePatientIdReturnsHeaderId() {
		Long resolved = ReflectionTestUtils.invokeMethod(controller, "resolvePatientId", 5L, (Object) null);

		assertThat(resolved).isEqualTo(5L);
	}

	@Test
	void resolvePatientIdThrowsWhenHeaderAndPrincipalAreNull() {
		assertThatThrownBy(
				() -> ReflectionTestUtils.invokeMethod(controller, "resolvePatientId", (Object) null, (Object) null))
			.isInstanceOf(AccessDeniedException.class);
	}

	@Test
	void resolvePatientIdParsesPrincipalName() {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("1");

		Long resolved = ReflectionTestUtils.invokeMethod(controller, "resolvePatientId", (Object) null, principal);

		assertThat(resolved).isEqualTo(1L);
	}

}
