package com.butter.wypl.schedule.data.request;

import java.time.LocalDateTime;
import java.util.List;

import com.butter.wypl.schedule.data.ModificationType;
import com.butter.wypl.schedule.data.response.MemberIdResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ScheduleUpdateRequest(
	String title,
	String description,

	@JsonProperty("start_datetime")
	LocalDateTime startDateTime,

	@JsonProperty("end_datetime")
	LocalDateTime endDateTime,

	@JsonProperty("modification_type")
	ModificationType modificationType,

	RepetitionRequest repetition,

	@JsonProperty("label_id")
	Integer labelId,

	List<MemberIdResponse> members
) {

}
