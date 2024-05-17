package com.butter.wypl.infrastructure.ouath.google;

import com.butter.wypl.infrastructure.ouath.OAuthResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleOAuthResponse(
		@JsonProperty("access_token")
		String accessToken,
		@JsonProperty("expires_in")
		long expiresIn,
		@JsonProperty("token_type")
		String tokenType,
		@JsonProperty("id_token")
		String idToken
) implements OAuthResponse {
}
