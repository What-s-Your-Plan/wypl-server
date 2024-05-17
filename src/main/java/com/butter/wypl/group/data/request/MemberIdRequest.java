package com.butter.wypl.group.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberIdRequest(
	@JsonProperty("member_id")
	int memberId
) {
}
