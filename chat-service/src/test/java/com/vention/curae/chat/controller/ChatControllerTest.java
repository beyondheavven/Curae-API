package com.vention.curae.chat.controller;

import com.vention.curae.chat.dto.ChatCreateRequestDto;
import com.vention.curae.chat.dto.ChatResponseDto;
import com.vention.curae.chat.mapper.ChatMapper;
import com.vention.curae.chat.message.dto.MessageResponseDto;
import com.vention.curae.chat.message.dto.MessageSendRequestDto;
import com.vention.curae.chat.message.model.Message;
import com.vention.curae.chat.model.Chat;
import com.vention.curae.chat.service.ChatService;
import com.vention.curae.wrapper.ResponseWrapper;
import java.security.Principal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

	@Mock
	private ChatService chatService;

	@Mock
	private ChatMapper chatMapper;

	@Mock
	private Principal principal;

	private ChatController chatController;

	@BeforeEach
	void setUp() {
		chatController = new ChatController(chatService, chatMapper);
	}

	@Test
	void createChatDelegatesAndReturnsCreatedWrapper() {
		ChatCreateRequestDto dto = mock(ChatCreateRequestDto.class);
		Chat chat = mock(Chat.class);
		ChatResponseDto responseDto = mock(ChatResponseDto.class);
		when(principal.getName()).thenReturn("1");
		when(dto.doctorId()).thenReturn(2L);
		when(chatService.createChat(1L, 2L)).thenReturn(chat);
		when(chatMapper.toChatResponse(chat)).thenReturn(responseDto);

		ResponseWrapper<ChatResponseDto> result = chatController.createChat(principal, dto);

		assertEquals(HttpStatus.CREATED, result.getStatus());
		assertEquals(HttpStatus.CREATED.value(), result.getStatusCode());
		assertEquals(responseDto, result.getData());
		verify(chatService).createChat(1L, 2L);
		verify(chatMapper).toChatResponse(chat);
	}

	@Test
	void getMyChatsDelegatesAndReturnsMappedChats() {
		Chat chat = mock(Chat.class);
		ChatResponseDto responseDto = mock(ChatResponseDto.class);
		when(principal.getName()).thenReturn("1");
		when(chatService.getUserChats(1L)).thenReturn(List.of(chat));
		when(chatMapper.toChatResponse(chat)).thenReturn(responseDto);

		ResponseWrapper<List<ChatResponseDto>> result = chatController.getMyChats(principal);

		assertEquals(HttpStatus.OK, result.getStatus());
		assertNotNull(result.getData());
		assertEquals(1, result.getData().size());
		assertEquals(responseDto, result.getData().get(0));
		verify(chatService).getUserChats(1L);
		verify(chatMapper).toChatResponse(chat);
	}

	@Test
	void getMessagesDelegatesAndReturnsMappedPage() {
		Message message = mock(Message.class);
		MessageResponseDto responseDto = mock(MessageResponseDto.class);
		Pageable pageable = PageRequest.of(0, 20);
		Page<Message> page = new PageImpl<>(List.of(message), pageable, 1);
		when(principal.getName()).thenReturn("1");
		when(chatService.getChatMessages(10L, 1L, pageable)).thenReturn(page);
		when(chatMapper.toMessageResponse(message)).thenReturn(responseDto);

		ResponseWrapper<Page<MessageResponseDto>> result = chatController.getMessages(principal, 10L, pageable);

		assertEquals(HttpStatus.OK, result.getStatus());
		assertNotNull(result.getData());
		assertEquals(1, result.getData().getTotalElements());
		assertEquals(responseDto, result.getData().getContent().get(0));
		verify(chatService).getChatMessages(10L, 1L, pageable);
		verify(chatMapper).toMessageResponse(message);
	}

	@Test
	void sendMessageDelegatesAndReturnsCreatedWrapper() {
		MessageSendRequestDto dto = mock(MessageSendRequestDto.class);
		Message message = mock(Message.class);
		MessageResponseDto responseDto = mock(MessageResponseDto.class);
		when(principal.getName()).thenReturn("1");
		when(dto.content()).thenReturn("hello");
		when(chatService.createMessage(10L, 1L, "hello")).thenReturn(message);
		when(chatMapper.toMessageResponse(message)).thenReturn(responseDto);

		ResponseWrapper<MessageResponseDto> result = chatController.sendMessage(principal, 10L, dto);

		assertEquals(HttpStatus.CREATED, result.getStatus());
		assertEquals(HttpStatus.CREATED.value(), result.getStatusCode());
		assertEquals(responseDto, result.getData());
		verify(chatService).createMessage(10L, 1L, "hello");
		verify(chatMapper).toMessageResponse(message);
	}

}
