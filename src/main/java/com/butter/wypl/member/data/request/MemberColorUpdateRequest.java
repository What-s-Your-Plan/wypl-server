package com.butter.wypl.member.data.request;

import com.butter.wypl.global.common.Color;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberColorUpdateRequest(
		@JsonProperty("color")
		Color color
) {
}
