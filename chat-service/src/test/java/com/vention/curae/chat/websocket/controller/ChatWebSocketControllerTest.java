package com.vention.curae.chat.websocket.controller;

import com.vention.curae.chat.service.ChatService;
import com.vention.curae.chat.websocket.dto.ChatMessageSendWsDto;
import java.security.Principal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatWebSocketControllerTest {

	@Mock
	private ChatService chatService;

	@InjectMocks
	private ChatWebSocketController chatWebSocketController;

	@Test
	void sendMessageWithNullPrincipalThrowsAccessDenied() {
		ChatMessageSendWsDto dto = mock(ChatMessageSendWsDto.class);

		assertThrows(AccessDeniedException.class, () -> chatWebSocketController.sendMessage(10L, null, dto));

		verifyNoInteractions(chatService);
	}

	@Test
	void sendMessageWithNullPrincipalNameThrowsAccessDenied() {
		ChatMessageSendWsDto dto = mock(ChatMessageSendWsDto.class);
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn(null);

		assertThrows(AccessDeniedException.class, () -> chatWebSocketController.sendMessage(10L, principal, dto));

		verifyNoInteractions(chatService);
	}

	@Test
	void sendMessageWithValidPrincipalDelegatesToService() {
		ChatMessageSendWsDto dto = mock(ChatMessageSendWsDto.class);
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("1");
		when(dto.content()).thenReturn("hello");

		chatWebSocketController.sendMessage(10L, principal, dto);

		verify(chatService).createMessage(10L, 1L, "hello");
	}

}
