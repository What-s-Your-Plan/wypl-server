package com.butter.wypl.auth.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.auth.exception.AuthErrorCode;
import com.butter.wypl.auth.exception.AuthException;
import com.butter.wypl.auth.fixture.OAuthMemberFixture;
import com.butter.wypl.global.annotation.ServiceTest;
import com.butter.wypl.infrastructure.ouath.OAuthMember;
import com.butter.wypl.member.domain.Member;

@ServiceTest
class SignInServiceTest {

	@Autowired
	private SignInService signInService;

	@DisplayName("구글 회원가입에 성공한다.")
	@ParameterizedTest
	@EnumSource(OAuthMemberFixture.class)
	void signInSuccessWithGoogle(OAuthMemberFixture oAuthMemberFixture) {
		/* Given */
		OAuthMember oAuthMember = oAuthMemberFixture.toGoogleOAuthMember();
		String provider = "google";

		/* When */
		Member member = signInService.signIn(oAuthMember, provider);

		/* Then */
		assertThat(member).isNotNull();
	}

	@DisplayName("지원하지 않는 로그인은 예외를 던진다.")
	@Test
	void invalidProvider() {
		/* Given */
		OAuthMember oAuthMember = OAuthMemberFixture.GOOGLE_OAUTH_MEMBER.toGoogleOAuthMember();
		String provider = "";

		/* When */
		/* Then */
		assertThatThrownBy(() -> signInService.signIn(oAuthMember, provider))
				.isInstanceOf(AuthException.class)
				.hasMessageContaining(AuthErrorCode.NO_SUPPORT_PROVIDER.getMessage());
	}
}