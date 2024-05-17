package com.butter.wypl.member.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberNicknameUpdateResponse(
		@JsonProperty("nickname")
		String nickname
) {
}
