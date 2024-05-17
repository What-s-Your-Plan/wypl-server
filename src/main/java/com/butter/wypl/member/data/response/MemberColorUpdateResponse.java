package com.butter.wypl.member.data.response;

import com.butter.wypl.global.common.Color;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberColorUpdateResponse(
		@JsonProperty("color")
		Color color
) {

	public static MemberColorUpdateResponse from(Color color) {
		return new MemberColorUpdateResponse(color);
	}
}
