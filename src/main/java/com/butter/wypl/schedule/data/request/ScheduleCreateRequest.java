package com.butter.wypl.schedule.data.request;

import java.time.LocalDateTime;
import java.util.List;

import com.butter.wypl.label.domain.Label;
import com.butter.wypl.schedule.data.response.MemberIdResponse;
import com.butter.wypl.schedule.domain.Category;
import com.butter.wypl.schedule.domain.Schedule;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record ScheduleCreateRequest(
	String title,
	String description,

	@JsonProperty("start_date")
	LocalDateTime startDate,

	@JsonProperty("end_date")
	LocalDateTime endDate,
	Category category,

	@JsonProperty("group_id")
	Integer groupId,

	RepetitionRequest repetition,

	@JsonProperty("label_id")
	Integer labelId,

	List<MemberIdResponse> members
) {

	public Schedule toEntity(Label label) {
		return null;
	}

	public static ScheduleCreateRequest of(Schedule schedule, List<MemberIdResponse> members) {
		return null;
	}
}
