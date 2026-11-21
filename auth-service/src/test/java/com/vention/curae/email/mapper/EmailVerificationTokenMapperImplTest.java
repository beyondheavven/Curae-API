package com.vention.curae.email.mapper;

import com.vention.curae.email.model.EmailTokenStatus;
import com.vention.curae.email.model.EmailTokenType;
import com.vention.curae.email.model.EmailVerificationToken;
import com.vention.curae.user.model.User;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class EmailVerificationTokenMapperImplTest {

	private final EmailVerificationTokenMapperImpl mapper = new EmailVerificationTokenMapperImpl();

	@Test
	void buildTokenReturnsNullWhenAllNull() {
		assertThat(mapper.buildToken(null, null, null, null)).isNull();
	}

	@Test
	void buildTokenMapsFields() {
		User user = mock(User.class);
		Instant expiresAt = Instant.parse("2026-01-01T00:00:00Z");

		EmailVerificationToken token = mapper.buildToken(user, "hash", expiresAt, EmailTokenType.VERIFY_EMAIL);

		assertThat(token.getUser()).isEqualTo(user);
		assertThat(token.getTokenHash()).isEqualTo("hash");
		assertThat(token.getExpiresAt()).isEqualTo(expiresAt);
		assertThat(token.getType()).isEqualTo(EmailTokenType.VERIFY_EMAIL);
		assertThat(token.getStatus()).isEqualTo(EmailTokenStatus.SENT);
	}

	@Test
	void createResetPasswordTokenReturnsNullWhenAllNull() {
		assertThat(mapper.createResetPasswordToken(null, null, null, null)).isNull();
	}

	@Test
	void createResetPasswordTokenMapsFields() {
		User user = mock(User.class);
		Instant expiresAt = Instant.parse("2026-01-01T00:00:00Z");

		EmailVerificationToken token = mapper.createResetPasswordToken(user, "123456", "hash", expiresAt);

		assertThat(token.getUser()).isEqualTo(user);
		assertThat(token.getCode()).isEqualTo("123456");
		assertThat(token.getTokenHash()).isEqualTo("hash");
		assertThat(token.getExpiresAt()).isEqualTo(expiresAt);
		assertThat(token.getResendCount()).isZero();
		assertThat(token.getStatus()).isEqualTo(EmailTokenStatus.SENT);
		assertThat(token.getType()).isEqualTo(EmailTokenType.RESET_PASSWORD);
	}

}
