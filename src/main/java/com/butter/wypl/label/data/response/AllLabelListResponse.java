package com.butter.wypl.label.data.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AllLabelListResponse(

	@JsonProperty("label_count")
	int labelCount,

	List<AllLabelResponse> labels
) {

	public static AllLabelListResponse from(List<AllLabelResponse> allLabelResponses) {
		return new AllLabelListResponse(allLabelResponses.size(), allLabelResponses);
	}
}
