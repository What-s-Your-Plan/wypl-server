package com.butter.wypl.auth.data.response;

import com.butter.wypl.auth.data.JsonWebTokens;
import com.butter.wypl.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthTokensResponse(
		@JsonProperty("member_id")
		int memberId,
		@JsonProperty("access_token")
		String accessToken,
		@JsonProperty("refresh_token")
		String refreshToken
) {

	public static AuthTokensResponse of(
			final Member member,
			final JsonWebTokens jsonWebTokens
	) {
		return new AuthTokensResponse(
				member.getId(),
				jsonWebTokens.accessToken(),
				jsonWebTokens.refreshToken()
		);
	}
}
