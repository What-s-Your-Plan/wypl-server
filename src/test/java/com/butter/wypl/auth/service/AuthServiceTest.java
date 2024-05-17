package com.butter.wypl.auth.service;

import static com.butter.wypl.auth.fixture.OAuthMemberFixture.*;
import static com.butter.wypl.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.auth.data.JsonWebTokens;
import com.butter.wypl.auth.data.response.AuthTokensResponse;
import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.auth.domain.RefreshToken;
import com.butter.wypl.auth.domain.RefreshTokenRepository;
import com.butter.wypl.auth.exception.AuthErrorCode;
import com.butter.wypl.auth.exception.AuthException;
import com.butter.wypl.auth.utils.JwtProvider;
import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.infrastructure.ouath.OAuthMember;
import com.butter.wypl.infrastructure.ouath.OAuthMemberProvider;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.repository.MemberRepository;

@MockServiceTest
class AuthServiceTest {

	@InjectMocks
	private AuthService authService;
	@Mock
	private SignInService signInService;
	@Mock
	private OAuthMemberProvider oAuthMemberProvider;
	@Mock
	private JwtProvider jwtProvider;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@DisplayName("JsonWebTokens 발급에 성공한다.")
	@Test
	void generateTokensSuccess() {
		/* Given */
		String googleProvider = "google";
		String dummyCode = "dummy_code";
		given(oAuthMemberProvider.getOAuthMember(googleProvider, dummyCode))
				.willReturn(GOOGLE_OAUTH_MEMBER.toGoogleOAuthMember());

		Member member = KIM_JEONG_UK.toMember();
		given(memberRepository.findByEmail(member.getEmail()))
				.willReturn(Optional.of(member));

		JsonWebTokens tokens = new JsonWebTokens("at", "rt");
		given(jwtProvider.generateJsonWebTokens(any(Integer.class)))
				.willReturn(tokens);

		RefreshToken refreshToken = RefreshToken.of(0, tokens.refreshToken());
		given(refreshTokenRepository.save(any(RefreshToken.class)))
				.willReturn(refreshToken);

		/* When */
		AuthTokensResponse response = authService.generateTokens(googleProvider, dummyCode);

		/* Then */
		assertAll(
				() -> assertThat(response.accessToken()).isNotNull(),
				() -> assertThat(response.refreshToken()).isNotNull()
		);
	}

	@DisplayName("신규 회원의 JsonWebToken 발급에 성공한다.")
	@Test
	void getMemberElseGetTest() {
		/* Given */
		String googleProvider = "google";
		String dummyCode = "dummy_code";
		given(oAuthMemberProvider.getOAuthMember(googleProvider, dummyCode))
				.willReturn(GOOGLE_OAUTH_MEMBER.toGoogleOAuthMember());

		Member member = KIM_JEONG_UK.toMember();
		given(memberRepository.findByEmail(member.getEmail()))
				.willReturn(Optional.empty());

		JsonWebTokens tokens = new JsonWebTokens("at", "rt");
		given(jwtProvider.generateJsonWebTokens(any(Integer.class)))
				.willReturn(tokens);

		given(signInService.signIn(any(OAuthMember.class), anyString()))
				.willReturn(member);

		RefreshToken refreshToken = RefreshToken.of(0, tokens.refreshToken());
		given(refreshTokenRepository.save(any(RefreshToken.class)))
				.willReturn(refreshToken);

		/* When */
		AuthTokensResponse response = authService.generateTokens(googleProvider, dummyCode);

		/* Then */
		assertAll(
				() -> assertThat(response.accessToken()).isNotNull(),
				() -> assertThat(response.refreshToken()).isNotNull()
		);
	}

	@DisplayName("reissueTokensTest")
	@Nested
	class ReissueTokensTest {
		@DisplayName("토큰 재발급에 성공한다.")
		@Test
		void reissueTokensTest() {
			/* Given */
			int memberId = 1;
			Member member = KIM_JEONG_UK.toMember();

			given(jwtProvider.getPayloadByRefreshToken(anyString()))
					.willReturn(memberId);

			given(memberRepository.findById(memberId))
					.willReturn(Optional.of(member));

			given(refreshTokenRepository.findById(memberId))
					.willReturn(Optional.of(RefreshToken.of(memberId, "rt")));

			given(jwtProvider.generateJsonWebTokens(any(Integer.class)))
					.willReturn(new JsonWebTokens("at", "rt"));

			/* When */
			AuthTokensResponse response = authService.reissueTokens("rt");

			/* Then */
			assertAll(
					() -> assertThat(response.accessToken()).isNotNull(),
					() -> assertThat(response.refreshToken()).isNotNull()
			);
		}

		@DisplayName("Redis에 토큰이 없으면 예외를 던진다.")
		@Test
		void tokenIsNotExisted() {
			/* Given */
			int memberId = 1;
			Member member = KIM_JEONG_UK.toMember();

			given(jwtProvider.getPayloadByRefreshToken(anyString()))
					.willReturn(memberId);

			given(memberRepository.findById(memberId))
					.willReturn(Optional.of(member));

			given(refreshTokenRepository.findById(memberId))
					.willReturn(Optional.of(RefreshToken.of(memberId, "rt")));

			/* When & Then */
			assertThatThrownBy(() -> authService.reissueTokens("at"))
					.isInstanceOf(AuthException.class)
					.hasMessageContaining(AuthErrorCode.INVALID_JWT.getMessage());
		}

		@DisplayName("Redis에 토큰과 동일하지 않은 토큰이면 예외를 던진다.")
		@Test
		void tokenIsNotEquals() {
			/* Given */
			int memberId = 1;
			Member member = KIM_JEONG_UK.toMember();

			given(jwtProvider.getPayloadByRefreshToken(anyString()))
					.willReturn(memberId);

			given(memberRepository.findById(memberId))
					.willReturn(Optional.of(member));

			given(refreshTokenRepository.findById(memberId))
					.willReturn(Optional.empty());

			/* When & Then */
			assertThatThrownBy(() -> authService.reissueTokens("rt"))
					.isInstanceOf(AuthException.class)
					.hasMessageContaining(AuthErrorCode.NON_EXISTED_TOKEN.getMessage());
		}
	}

	@DisplayName("로그아웃 테스트")
	@Nested
	class LogoutTest {

		private AuthMember authMember;
		private RefreshToken refreshToken;

		@BeforeEach
		void setUp() {
			authMember = AuthMember.from(1);
			refreshToken = RefreshToken.of(authMember.getId(), "rt");
		}

		@DisplayName("로그아웃에 성공한다.")
		@Test
		void logoutSuccess() {
			/* Given */
			given(refreshTokenRepository.findById(authMember.getId()))
					.willReturn(Optional.of(refreshToken));

			/* When */
			/* Then */
			assertThatCode(() -> authService.deleteToken(authMember))
					.doesNotThrowAnyException();
		}

		@DisplayName("로그아웃에 실패한다.")
		@Test
		void logoutFailed() {
			/* Given */
			given(refreshTokenRepository.findById(authMember.getId()))
					.willReturn(Optional.empty());

			/* When */
			/* Then */
			assertThatThrownBy(() -> authService.deleteToken(authMember))
					.isInstanceOf(AuthException.class)
					.hasMessageContaining(AuthErrorCode.NOT_AUTHORIZATION_MEMBER.getMessage());
		}
	}
}