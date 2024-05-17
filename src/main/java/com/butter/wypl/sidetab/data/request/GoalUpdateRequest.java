package com.butter.wypl.sidetab.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoalUpdateRequest(
		@JsonProperty("content")
		String content
) {
}
