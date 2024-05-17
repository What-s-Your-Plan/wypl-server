package com.butter.wypl.auth.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.auth.data.JsonWebTokens;
import com.butter.wypl.auth.utils.JwtProvider;
import com.butter.wypl.global.annotation.RedisRepositoryTest;

@RedisRepositoryTest
class RefreshTokenRepositoryTest {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private JwtProvider jwtProvider;

	@DisplayName("Refresh Token 저장에 성공한다.")
	@ParameterizedTest
	@ValueSource(ints = {1, Integer.MAX_VALUE})
	void refreshTokenSave(int memberId) {
		/* Given */
		JsonWebTokens tokens = generateTokens(memberId);
		RefreshToken refreshToken = RefreshToken.of(memberId, tokens.refreshToken());

		/* When */
		/* Then */
		assertThatCode(() -> {
			RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);
			assertAll(
					() -> assertThat(savedRefreshToken.getMemberId()).isEqualTo(memberId),
					() -> assertThat(savedRefreshToken.getToken()).isEqualTo(tokens.refreshToken())
			);
		}).doesNotThrowAnyException();
	}

	@DisplayName("Refresh Token 삭제에 성공한다.")
	@ParameterizedTest
	@ValueSource(ints = {1, Integer.MAX_VALUE})
	void refreshTokenDelete(int memberId) {
		/* Given */
		JsonWebTokens tokens = generateTokens(memberId);
		RefreshToken refreshToken = RefreshToken.of(memberId, tokens.refreshToken());
		RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

		/* When */
		refreshTokenRepository.delete(savedRefreshToken);

		/* Then */
		assertThat(refreshTokenRepository.existsById(memberId)).isFalse();
	}

	private JsonWebTokens generateTokens(
			final int memberId
	) {
		return jwtProvider.generateJsonWebTokens(memberId);
	}
}