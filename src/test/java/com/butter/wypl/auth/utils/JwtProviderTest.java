package com.butter.wypl.auth.utils;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.auth.data.JsonWebTokens;
import com.butter.wypl.auth.exception.AuthErrorCode;
import com.butter.wypl.auth.exception.AuthException;
import com.butter.wypl.global.annotation.ServiceTest;

@ServiceTest
class JwtProviderTest {

	@Autowired
	private JwtProvider jwtProvider;

	@DisplayName("JWT 토큰 발급에 성공한다.")
	@ParameterizedTest
	@ValueSource(ints = {1, Integer.MAX_VALUE})
	void generateTokens(int memberId) {
		/* Given */

		/* When */
		JsonWebTokens tokens = jwtProvider.generateJsonWebTokens(memberId);

		/* Then */
		assertAll(
				() -> assertThat(tokens.accessToken()).isNotNull(),
				() -> assertThat(tokens.refreshToken()).isNotNull()
		);
	}

	@DisplayName("JsonWebTokens 검증에 성공한다.")
	@Test
	void validateTokenTest() {
		/* Given */
		JsonWebTokens tokens = jwtProvider.generateJsonWebTokens(0);

		/* When & Then */
		assertThatCode(() -> {
			jwtProvider.validateToken(tokens.accessToken());
			jwtProvider.validateToken(tokens.refreshToken());
		}).doesNotThrowAnyException();
	}

	@DisplayName("Payload 조회에 성공한다.")
	@Test
	void getPayloadTest() {
		/* Given */
		int memberId = 0;
		JsonWebTokens tokens = jwtProvider.generateJsonWebTokens(memberId);

		/* When */
		int atMemberId = jwtProvider.getPayload(tokens.accessToken());

		/* Then */
		assertThat(atMemberId).isEqualTo(memberId);
	}

	@DisplayName("RefreshToken Payload 조회에 성공한다.")
	@Test
	void getPayloadByRefreshTokenTest() {
		/* Given */
		int memberId = 0;
		JsonWebTokens tokens = jwtProvider.generateJsonWebTokens(memberId);

		/* When */
		int rtMemberId = jwtProvider.getPayloadByRefreshToken(tokens.refreshToken());

		/* Then */
		assertThat(rtMemberId).isEqualTo(memberId);
	}

	@DisplayName("payload 추출에 실패한다.")
	@Test
	void parsePayloadFailedTest() {
		/* Given */
		String invalidPayload = "invalid";

		/* When & Then */
		assertThatThrownBy(() -> jwtProvider.validateToken(invalidPayload))
				.isInstanceOf(AuthException.class)
				.hasMessageContaining(AuthErrorCode.INVALID_JWT.getMessage());
	}

	@DisplayName("tokenType 추출에 실패한다.")
	@Test
	void tokenTypeFailedTest() {
		/* Given */
		String invalid = "header.payload.signature";

		/* When & Then */
		assertThatThrownBy(() -> jwtProvider.validateToken(invalid))
				.isInstanceOf(AuthException.class)
				.hasMessageContaining(AuthErrorCode.INVALID_JWT.getMessage());
	}
}