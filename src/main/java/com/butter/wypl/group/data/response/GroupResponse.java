package com.butter.wypl.group.data.response;

import com.butter.wypl.global.common.Color;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GroupResponse(
		@JsonProperty("id")
		int id,
		@JsonProperty("name")
		String name,
		@JsonProperty("color")
		Color color
) {
}
