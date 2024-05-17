package com.butter.wypl.infrastructure.ouath.google;

import com.butter.wypl.infrastructure.ouath.OAuthMember;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleOAuthMember(
		@JsonProperty("sub")
		String subject,
		@JsonProperty("email")
		String email,
		@JsonProperty("email_verified")
		boolean isEmailVerified,
		@JsonProperty("picture")
		String profileImage
) implements OAuthMember {
}
