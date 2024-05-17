package com.butter.wypl.schedule.data.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ScheduleIdListResponse(
	@JsonProperty("schedule_count")
	int scheduleCount,

	List<ScheduleIdResponse> schedules
) {

	public static ScheduleIdListResponse from(List<ScheduleIdResponse> schedules) {
		return new ScheduleIdListResponse(
			schedules.size(),
			schedules
		);
	}
}
