package com.butter.wypl.schedule.data.response;

import com.butter.wypl.schedule.domain.Schedule;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ScheduleIdResponse(
	@JsonProperty("schedule_id")
	int scheduleId
) {

	public static ScheduleIdResponse from(Schedule schedule) {
		return new ScheduleIdResponse(schedule.getScheduleId());
	}
}
