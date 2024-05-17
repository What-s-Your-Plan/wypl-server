package com.butter.wypl.calendar.data.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CalendarListResponse(

	@JsonProperty("schedule_count")
	int scheduleCount,

	@JsonProperty("schedules")
	List<CalendarResponse> schedules
) {

	public static CalendarListResponse from(List<CalendarResponse> schedules) {
		return new CalendarListResponse(
			schedules.size(),
			schedules
		);
	}
}
