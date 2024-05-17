package com.butter.wypl.infrastructure.ouath;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.auth.exception.AuthErrorCode;
import com.butter.wypl.auth.exception.AuthException;
import com.butter.wypl.auth.fixture.OAuthMemberFixture;
import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.infrastructure.ouath.google.GoogleOAuthProvider;

@MockServiceTest
class OAuthMemberProviderTest {

	@InjectMocks
	private OAuthMemberProvider oAuthMemberProvider;

	@Mock
	private GoogleOAuthProvider googleOAuthProvider;

	@DisplayName("구글 OAuthMember 를 정상적으로 조회합니다.")
	@Test
	void getOAuthMemberSuccess() {
		/* Given */
		String googleProvider = "google";
		String dummyCode = "dummy_code";
		given(googleOAuthProvider.getOAuthMember(dummyCode))
				.willReturn(OAuthMemberFixture.GOOGLE_OAUTH_MEMBER.toGoogleOAuthMember());

		/* When */
		OAuthMember oAuthMember = oAuthMemberProvider.getOAuthMember(googleProvider, dummyCode);

		/* Then */
		assertThat(oAuthMember).isNotNull();
		assertThat(oAuthMember.email()).isEqualTo(OAuthMemberFixture.GOOGLE_OAUTH_MEMBER.getEmail());
	}

	@DisplayName("지원 하지 않는 소셜로그인 제공자입니다.")
	@Test
	void noSupportProvider() {
		/* Given */
		String noSupportProvider = "";
		String dummyCode = "dummy_code";

		/* When */
		/* Then */
		assertThatThrownBy(() -> oAuthMemberProvider.getOAuthMember(noSupportProvider, dummyCode))
				.isInstanceOf(AuthException.class)
				.hasMessageContaining(AuthErrorCode.NO_SUPPORT_PROVIDER.getMessage());
	}
}