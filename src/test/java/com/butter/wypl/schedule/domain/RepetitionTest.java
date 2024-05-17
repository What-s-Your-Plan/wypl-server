package com.butter.wypl.schedule.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.butter.wypl.schedule.exception.ScheduleErrorCode;
import com.butter.wypl.schedule.exception.ScheduleException;
import com.butter.wypl.schedule.fixture.embedded.RepetitionFixture;

public class RepetitionTest {

	@Test
	@DisplayName("day of week 값 validate")
	void dayOfWeekValidation() {
		// Given
		Repetition repetition = RepetitionFixture.MONDAY_REPETITION.toRepetition();

		// When
		// Then
		assertThatThrownBy(() -> {
				Repetition.builder()
					.repetitionStartDate(repetition.getRepetitionStartDate())
					.repetitionEndDate(repetition.getRepetitionEndDate())
					.repetitionCycle(repetition.getRepetitionCycle())
					.week(repetition.getWeek())
					.dayOfWeek(128)
					.build();
			}
		).isInstanceOf(ScheduleException.class)
			.hasMessageContaining(ScheduleErrorCode.NOT_APPROPRIATE_DAY_OF_WEEK.getMessage());
	}

	@Test
	@DisplayName("반복 기간 validate")
	void durationValidation() {
		// Given
		Repetition repetition = RepetitionFixture.MONDAY_REPETITION.toRepetition();

		// When
		// Then
		assertThatThrownBy(() -> {
				Repetition.builder()
					.repetitionStartDate(LocalDate.of(2024, 5, 7))
					.repetitionEndDate(LocalDate.of(2024, 5, 4))
					.repetitionCycle(repetition.getRepetitionCycle())
					.week(repetition.getWeek())
					.dayOfWeek((repetition.getDayOfWeek()))
					.build();
			}
		).isInstanceOf(ScheduleException.class)
			.hasMessageContaining(ScheduleErrorCode.NOT_APPROPRIATE_REPETITION_DURATION.getMessage());

	}

}
