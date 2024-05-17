package com.butter.wypl.schedule.data.response;

import java.time.LocalDateTime;
import java.util.List;

import com.butter.wypl.label.data.response.LabelResponse;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.schedule.domain.Category;
import com.butter.wypl.schedule.domain.Schedule;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ScheduleDetailResponse(

	@JsonProperty("schedule_id")
	int scheduleId,
	String title,
	String description,

	@JsonProperty("start_date")
	LocalDateTime startDate,

	@JsonProperty("end_date")
	LocalDateTime endDate,
	Category category,

	@JsonProperty("group_id")
	Integer groupId,

	RepetitionResponse repetition,

	@JsonProperty("label")
	LabelResponse labelId,

	@JsonProperty("member_count")
	int member_count,
	List<MemberResponse> members
) {

	public static ScheduleDetailResponse of(
		Schedule schedule,
		List<Member> members
	) {
		return new ScheduleDetailResponse(
			schedule.getScheduleId(),
			schedule.getTitle(),
			schedule.getDescription(),
			schedule.getStartDate(),
			schedule.getEndDate(),
			schedule.getCategory(),
			schedule.getGroupId(),
			(schedule.getRepetition() == null) ? null : RepetitionResponse.from(schedule.getRepetition()),
			(schedule.getLabel() == null) ? null : LabelResponse.from(schedule.getLabel()),
			members.size(),
			MemberResponse.from(members)
		);
	}
}
