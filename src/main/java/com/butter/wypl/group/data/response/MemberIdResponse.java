package com.butter.wypl.group.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberIdResponse(
	@JsonProperty("member_id")
	int memberId
) {
}
