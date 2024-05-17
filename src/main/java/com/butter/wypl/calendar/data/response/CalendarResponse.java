package com.butter.wypl.calendar.data.response;

import java.time.LocalDateTime;

import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.label.data.response.LabelResponse;
import com.butter.wypl.schedule.domain.Category;
import com.butter.wypl.schedule.domain.Schedule;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CalendarResponse(
	@JsonProperty("schedule_id")
	int scheduleId,

	String title,

	String description,

	Category category,

	@JsonProperty("start_date")
	LocalDateTime startDate,

	@JsonProperty("end_date")
	LocalDateTime endDate,

	LabelResponse label,

	GroupResponse group
) {

	public static CalendarResponse of(Schedule schedule, MemberGroup memberGroup) {
		return new CalendarResponse(
			schedule.getScheduleId(),
			schedule.getTitle(),
			schedule.getDescription(),
			schedule.getCategory(),
			schedule.getStartDate(),
			schedule.getEndDate(),
			LabelResponse.from(schedule.getLabel()),
			memberGroup == null ? null : GroupResponse.from(memberGroup)
		);
	}
}
