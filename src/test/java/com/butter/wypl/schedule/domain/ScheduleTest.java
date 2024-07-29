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

		}

		@Test
		@DisplayName("일정 제목 null validation")
		void titleValidationNull() {

		}

		@Test
		@DisplayName("일정 제목 길이 validation")
		void titleValidationLen() {

		}
	}

	@Test
	@DisplayName("startDate, endDate validation")
	void durationValidation() {

	}
}
