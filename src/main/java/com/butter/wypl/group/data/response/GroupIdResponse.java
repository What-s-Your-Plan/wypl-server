package com.butter.wypl.group.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GroupIdResponse(
		@JsonProperty("group_id")
		int groupId
) {
}