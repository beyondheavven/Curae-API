package com.vention.curae.chat.mapper;

import com.vention.curae.chat.dto.ChatResponseDto;
import com.vention.curae.chat.message.dto.MessageResponseDto;
import com.vention.curae.chat.message.model.Message;
import com.vention.curae.chat.model.Chat;
import java.time.Instant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChatMapperImplTest {

	private final ChatMapperImpl chatMapper = new ChatMapperImpl();

	@Test
	void toChatResponseWithNullReturnsNull() {
		assertNull(chatMapper.toChatResponse(null));
	}

	@Test
	void toChatResponseMapsFields() {
		Chat chat = mock(Chat.class);
		Instant createdAt = Instant.parse("2026-01-01T00:00:00Z");
		when(chat.getId()).thenReturn(1L);
		when(chat.getPatientId()).thenReturn(2L);
		when(chat.getDoctorId()).thenReturn(3L);
		when(chat.getCreatedAt()).thenReturn(createdAt);

		ChatResponseDto result = chatMapper.toChatResponse(chat);

		assertEquals(1L, result.id());
		assertEquals(2L, result.patientId());
		assertEquals(3L, result.doctorId());
		assertEquals(createdAt, result.createdAt());
	}

	@Test
	void toMessageResponseWithNullReturnsNull() {
		assertNull(chatMapper.toMessageResponse(null));
	}

	@Test
	void toMessageResponseMapsFields() {
		Message message = mock(Message.class);
		Instant createdAt = Instant.parse("2026-01-01T00:00:00Z");
		when(message.getId()).thenReturn(11L);
		when(message.getChatId()).thenReturn(22L);
		when(message.getSenderId()).thenReturn(33L);
		when(message.getContent()).thenReturn("hello");
		when(message.getCreatedAt()).thenReturn(createdAt);

		MessageResponseDto result = chatMapper.toMessageResponse(message);

		assertEquals(11L, result.id());
		assertEquals(22L, result.chatId());
		assertEquals(33L, result.senderId());
		assertEquals("hello", result.content());
		assertEquals(createdAt, result.createdAt());
	}

}
