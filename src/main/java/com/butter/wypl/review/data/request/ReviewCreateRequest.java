package com.butter.wypl.review.data.request;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record ReviewCreateRequest(

	String title,

	@JsonProperty("schedule_id")
	int scheduleId,

	List<Map<String, Object>> contents

) {
}
