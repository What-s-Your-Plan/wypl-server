package com.butter.wypl.infrastructure.ouath;

public interface OAuthClient {
	OAuthResponse getOAuthMember(final String code);
}
