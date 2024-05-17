package com.butter.wypl.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.auth.data.JsonWebTokens;
import com.butter.wypl.auth.data.response.AuthTokensResponse;
import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.auth.domain.RefreshToken;
import com.butter.wypl.auth.domain.RefreshTokenRepository;
import com.butter.wypl.auth.exception.AuthErrorCode;
import com.butter.wypl.auth.exception.AuthException;
import com.butter.wypl.auth.utils.JwtProvider;
import com.butter.wypl.infrastructure.ouath.OAuthMember;
import com.butter.wypl.infrastructure.ouath.OAuthMemberProvider;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.member.utils.MemberServiceUtils;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

	private final SignInService signInService;

	private final JwtProvider jwtProvider;
	private final OAuthMemberProvider oAuthMemberProvider;

	private final RefreshTokenRepository refreshTokenRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public AuthTokensResponse generateTokens(
			final String provider,
			final String code
	) {
		OAuthMember oAuthMember = oAuthMemberProvider.getOAuthMember(provider, code);
		Member member = getMember(provider, oAuthMember);
		JsonWebTokens tokens = generateJsonWebTokens(member);
		return AuthTokensResponse.of(member, tokens);
	}

	@Transactional
	public AuthTokensResponse reissueTokens(
			final String refreshToken
	) {
		int memberId = jwtProvider.getPayloadByRefreshToken(refreshToken);

		Member findMember = MemberServiceUtils.findById(memberRepository, memberId);
		RefreshToken token = refreshTokenRepository.findById(memberId)
				.orElseThrow(() -> new AuthException(AuthErrorCode.NON_EXISTED_TOKEN));

		if (token.isEqualsToken(refreshToken)) {
			JsonWebTokens tokens = generateJsonWebTokens(findMember);
			return AuthTokensResponse.of(findMember, tokens);
		}

		throw new AuthException(AuthErrorCode.INVALID_JWT);
	}

	private Member getMember(String provider, OAuthMember oAuthMember) {
		return memberRepository.findByEmail(oAuthMember.email())
				.orElseGet(() -> signInService.signIn(oAuthMember, provider));
	}

	private JsonWebTokens generateJsonWebTokens(Member member) {
		JsonWebTokens tokens = jwtProvider.generateJsonWebTokens(member.getId());
		refreshTokenRepository.save(RefreshToken.of(member.getId(), tokens.refreshToken()));
		return tokens;
	}

	@Transactional
	public void deleteToken(AuthMember authMember) {
		RefreshToken refreshToken = refreshTokenRepository.findById(authMember.getId())
				.orElseThrow(() -> new AuthException(AuthErrorCode.NOT_AUTHORIZATION_MEMBER));

		refreshTokenRepository.delete(refreshToken);
	}
}
