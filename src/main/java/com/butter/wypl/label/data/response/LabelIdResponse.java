package com.butter.wypl.label.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LabelIdResponse(
	@JsonProperty("label_id")
	int labelId
) {
	public static LabelIdResponse from(int labelId) {
		return new LabelIdResponse(labelId);
	}
}
