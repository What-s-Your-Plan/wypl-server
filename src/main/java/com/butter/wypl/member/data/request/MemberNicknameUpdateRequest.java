package com.butter.wypl.member.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberNicknameUpdateRequest(
		@JsonProperty("nickname")
		String nickname
) {
}
