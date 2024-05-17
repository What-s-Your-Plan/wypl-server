package com.butter.wypl.schedule.data.response;

import java.time.LocalDate;

import com.butter.wypl.schedule.domain.Repetition;
import com.butter.wypl.schedule.domain.RepetitionCycle;
import com.fasterxml.jackson.annotation.JsonProperty;

public record RepetitionResponse(
	@JsonProperty("repetition_id")
	int repetitionId,

	@JsonProperty("repetition_cycle")
	RepetitionCycle repetitionCycle,

	@JsonProperty("repetition_start_date")
	LocalDate repetitionStartDate,

	@JsonProperty("repetition_end_date")
	LocalDate repetitionEndDate,

	@JsonProperty("day_of_week")
	int dayOfWeek,
	Integer week
) {

	public static RepetitionResponse from(Repetition repetition) {
		return new RepetitionResponse(
			repetition.getRepetitionId(),
			repetition.getRepetitionCycle(),
			repetition.getRepetitionStartDate(),
			repetition.getRepetitionEndDate(),
			repetition.getDayOfWeek(),
			repetition.getWeek()
		);
	}
}
