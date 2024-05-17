package com.butter.wypl.schedule.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.butter.wypl.schedule.exception.ScheduleErrorCode;
import com.butter.wypl.schedule.exception.ScheduleException;
import com.butter.wypl.schedule.fixture.ScheduleFixture;

public class ScheduleTest {

	@Nested
	class titleValidateTest {
		@Test
		@DisplayName("일정 제목 빈 문자열 validation")
		void titleValidation() {
			// Given
			Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			// When
			// Then
			assertThatThrownBy(() -> {
					Schedule.builder()
						.title("")
						.startDate(schedule.getStartDate())
						.endDate(schedule.getEndDate())
						.description(schedule.getDescription())
						.repetition(schedule.getRepetition())
						.category(schedule.getCategory())
						.groupId(schedule.getGroupId())
						.build();
				}
			).isInstanceOf(ScheduleException.class)
				.hasMessageContaining(ScheduleErrorCode.NOT_APPROPRIATE_TITLE.getMessage());
		}

		@Test
		@DisplayName("일정 제목 null validation")
		void titleValidationNull() {
			// Given
			Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			// When
			// Then
			assertThatThrownBy(() -> {
					Schedule.builder()
						.title(null)
						.startDate(schedule.getStartDate())
						.endDate(schedule.getEndDate())
						.description(schedule.getDescription())
						.repetition(schedule.getRepetition())
						.category(schedule.getCategory())
						.groupId(schedule.getGroupId())
						.build();
				}
			).isInstanceOf(ScheduleException.class)
				.hasMessageContaining(ScheduleErrorCode.NOT_APPROPRIATE_TITLE.getMessage());
		}

		@Test
		@DisplayName("일정 제목 길이 validation")
		void titleValidationLen() {
			// Given
			Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			// When
			// Then
			assertThatThrownBy(() -> {
					Schedule.builder()
						.title(
							"dkdkdkdkdkdasdaffffffffffffffffffffffddddddddddddddddddddddddddddddddddssssssssssssssssssssssssssssssssssssssss")
						.startDate(schedule.getStartDate())
						.endDate(schedule.getEndDate())
						.description(schedule.getDescription())
						.repetition(schedule.getRepetition())
						.category(schedule.getCategory())
						.groupId(schedule.getGroupId())
						.build();
				}
			).isInstanceOf(ScheduleException.class)
				.hasMessageContaining(ScheduleErrorCode.NOT_APPROPRIATE_TITLE.getMessage());
		}
	}

	@Test
	@DisplayName("startDate, endDate validation")
	void durationValidation() {
		// Given
		Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
		// When
		// Then
		assertThatThrownBy(() -> {
				Schedule.builder()
					.title(schedule.getTitle())
					.startDate(LocalDateTime.of(2024, 5, 7, 0, 0))
					.endDate(LocalDateTime.of(2024, 5, 6, 0, 0))
					.description(schedule.getDescription())
					.repetition(schedule.getRepetition())
					.category(schedule.getCategory())
					.groupId(schedule.getGroupId())
					.build();
			}
		).isInstanceOf(ScheduleException.class)
			.hasMessageContaining(ScheduleErrorCode.NOT_APPROPRIATE_DURATION.getMessage());

	}
}
