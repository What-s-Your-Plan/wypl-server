package com.butter.wypl.schedule.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberIdResponse(
	@JsonProperty("member_id")
	int memberId
) {
}
