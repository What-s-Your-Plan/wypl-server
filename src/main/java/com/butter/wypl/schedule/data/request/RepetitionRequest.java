package com.butter.wypl.schedule.data.request;

import java.time.LocalDate;

import com.butter.wypl.schedule.domain.Repetition;
import com.butter.wypl.schedule.domain.RepetitionCycle;
import com.fasterxml.jackson.annotation.JsonProperty;

public record RepetitionRequest(
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

	public Repetition toEntity() {
		return Repetition.builder()
			.repetitionCycle(repetitionCycle)
			.repetitionStartDate(repetitionStartDate)
			.repetitionEndDate(repetitionEndDate)
			.dayOfWeek(dayOfWeek)
			.week(week)
			.build();
	}

	public static RepetitionRequest from(Repetition repetition) {
		return new RepetitionRequest(
			repetition.getRepetitionCycle(),
			repetition.getRepetitionStartDate(),
			repetition.getRepetitionEndDate(),
			repetition.getDayOfWeek(),
			repetition.getWeek()
		);
	}
}
