package com.butter.wypl.infrastructure.ouath;

public interface OAuthMember {
	String email();

	String subject();

	String profileImage();

	default String getEmailPrefix() {
		return email().split("@")[0];
	}
}
