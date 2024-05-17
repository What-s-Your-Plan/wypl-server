package com.butter.wypl.schedule.fixture.embedded;

import java.time.LocalDate;

import com.butter.wypl.schedule.domain.Repetition;
import com.butter.wypl.schedule.domain.RepetitionCycle;

public enum RepetitionFixture {
	MONDAY_REPETITION(
		RepetitionCycle.WEEK,
		LocalDate.of(2024, 4, 25),
		LocalDate.of(2025, 4, 25),
		1,
		2
	),
	TUESDAY_THRUSDAY_REPETITION(
		RepetitionCycle.WEEK,
		LocalDate.of(2024, 4, 25),
		LocalDate.of(2025, 4, 25),
		2,
		36
	),
	MONTHLY_REPETITION(
		RepetitionCycle.MONTH,
		LocalDate.of(2024, 4, 25),
		LocalDate.of(2024, 7, 25),
		null,
		0
	),
	LAST_DAY_REPETITION(
		RepetitionCycle.MONTH,
		LocalDate.of(2024, 4, 25),
		LocalDate.of(2025, 4, 25),
		null,
		0
	),
	YEARLY_REPETITION(
		RepetitionCycle.YEAR,
		LocalDate.of(2024, 4, 25),
		null,
		null,
		0
	),
	;

	private final RepetitionCycle repetitionCycle;

	private final LocalDate repetitionStartDate;

	private final LocalDate repetitionEndDate;

	private final Integer week;

	private final int dayOfWeek;

	RepetitionFixture(RepetitionCycle repetitionCycle, LocalDate repetitionStartDate,
		LocalDate repetitionEndDate, Integer week, int dayOfWeek) {
		this.repetitionCycle = repetitionCycle;
		this.repetitionStartDate = repetitionStartDate;
		this.repetitionEndDate = repetitionEndDate;
		this.week = week;
		this.dayOfWeek = dayOfWeek;
	}

	public Repetition toRepetition() {
		return Repetition.builder()
			.repetitionCycle(repetitionCycle)
			.repetitionStartDate(repetitionStartDate)
			.repetitionEndDate(repetitionEndDate)
			.week(week)
			.dayOfWeek(dayOfWeek)
			.build();
	}

}
