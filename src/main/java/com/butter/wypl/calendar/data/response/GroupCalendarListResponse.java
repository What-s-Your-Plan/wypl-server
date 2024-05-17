package com.butter.wypl.calendar.data.response;

import java.util.List;

import com.butter.wypl.group.domain.MemberGroup;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GroupCalendarListResponse(
	GroupResponse group,

	@JsonProperty("schedule_count")
	int scheduleCount,

	List<GroupCalendarResponse> schedules
) {

	public static GroupCalendarListResponse of(List<GroupCalendarResponse> groupCalendarResponses,
		MemberGroup memberGroup) {
		return new GroupCalendarListResponse(
			GroupResponse.from(memberGroup),
			groupCalendarResponses.size(),
			groupCalendarResponses
		);
	}

}
