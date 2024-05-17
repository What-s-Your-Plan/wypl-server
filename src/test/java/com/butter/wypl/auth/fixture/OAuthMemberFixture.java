package com.butter.wypl.auth.fixture;

import com.butter.wypl.infrastructure.ouath.OAuthMember;
import com.butter.wypl.infrastructure.ouath.google.GoogleOAuthMember;

import lombok.Getter;

@Getter
public enum OAuthMemberFixture {
	GOOGLE_OAUTH_MEMBER("google_subject", "workju1124@gmail.com", "https://image.google.com/profile_image.png");

	private final String subject;
	private final String email;
	private final String profileImage;

	OAuthMemberFixture(String subject, String email, String profileImage) {
		this.subject = subject;
		this.email = email;
		this.profileImage = profileImage;
	}

	public OAuthMember toGoogleOAuthMember() {
		return new GoogleOAuthMember(subject, email, true, profileImage);
	}
}
