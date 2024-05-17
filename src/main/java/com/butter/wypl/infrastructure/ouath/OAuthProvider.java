package com.butter.wypl.infrastructure.ouath;

public interface OAuthProvider {
	OAuthMember getOAuthMember(final String code);
}
