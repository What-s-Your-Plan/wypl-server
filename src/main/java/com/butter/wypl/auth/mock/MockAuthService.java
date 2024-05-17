package com.butter.wypl.auth.mock;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.auth.data.JsonWebTokens;
import com.butter.wypl.auth.data.response.AuthTokensResponse;
import com.butter.wypl.auth.domain.RefreshToken;
import com.butter.wypl.auth.domain.RefreshTokenRepository;
import com.butter.wypl.auth.service.SignInService;
import com.butter.wypl.auth.utils.JwtProvider;
import com.butter.wypl.global.annotation.Generated;
import com.butter.wypl.infrastructure.ouath.google.GoogleOAuthMember;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Generated
@Profile({"local", "dev"})
@RequiredArgsConstructor
@Transactional
@Service
public class MockAuthService {

	private static final String GOOGLE = "google";

	private final SignInService signInService;

	private final JwtProvider jwtProvider;

	private final RefreshTokenRepository refreshTokenRepository;
	private final MemberRepository memberRepository;

	public AuthTokensResponse generateTokens(final String email) {
		Member member = getMember(email);
		JsonWebTokens tokens = generateJsonWebTokens(member);
		return AuthTokensResponse.of(member, tokens);
	}

	private Member getMember(final String email) {
		GoogleOAuthMember oAuthMember = new GoogleOAuthMember(
				String.valueOf(System.currentTimeMillis()),
				email,
				true,
				null);
		return memberRepository.findByEmail(email)
				.orElseGet(() -> signInService.signIn(oAuthMember, GOOGLE));
	}

	private JsonWebTokens generateJsonWebTokens(Member member) {
		JsonWebTokens tokens = jwtProvider.generateJsonWebTokens(member.getId());
		refreshTokenRepository.save(RefreshToken.of(member.getId(), tokens.refreshToken()));
		return tokens;
	}
}
