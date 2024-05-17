package com.butter.wypl.auth.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RefreshTokenTest {

	@DisplayName("isEqualsToken가 True를 반환한다.")
	@Test
	void isEqualsTokenReturnTrueTest() {
		/* Given */
		RefreshToken refreshToken = RefreshToken.of(1, "rt");

		/* When */
		boolean ret = refreshToken.isEqualsToken("rt");

		/* Then */
		assertThat(ret).isTrue();
	}

	@DisplayName("isEqualsToken가 False를 반환한다.")
	@Test
	void isEqualsTokenReturnFalseTest() {
		/* Given */
		RefreshToken refreshToken = RefreshToken.of(1, "at");

		/* When */
		boolean ret = refreshToken.isEqualsToken("rt");

		/* Then */
		assertThat(ret).isFalse();
	}
}