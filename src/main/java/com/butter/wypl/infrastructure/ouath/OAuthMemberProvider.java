package com.butter.wypl.infrastructure.ouath;

import org.springframework.stereotype.Component;

import com.butter.wypl.auth.exception.AuthErrorCode;
import com.butter.wypl.auth.exception.AuthException;
import com.butter.wypl.infrastructure.ouath.google.GoogleOAuthProvider;
import com.butter.wypl.member.domain.OauthProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuthMemberProvider {
	private final GoogleOAuthProvider googleOAuthProvider;

	public OAuthMember getOAuthMember(
			final String provider,
			final String code
	) {
		if (OauthProvider.GOOGLE.equalsName(provider)) {
			return googleOAuthProvider.getOAuthMember(code);
		}
		throw new AuthException(AuthErrorCode.NO_SUPPORT_PROVIDER);
	}
}
