package com.butter.wypl.sidetab.domain.embedded;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.butter.wypl.member.domain.CalendarTimeZone;
import com.butter.wypl.member.exception.MemberErrorCode;
import com.butter.wypl.member.exception.MemberException;
import com.butter.wypl.sidetab.fixture.SideTabFixture;

class DDayWidgetTest {

	@DisplayName("D-Day 위젯 생성에 성공한다.")
	void generateDDay() {
		/* Given */
		SideTabFixture sideTabFixture = SideTabFixture.SIDE_TAB_ONE;
		String title = sideTabFixture.getTitle();
		LocalDate dDay = sideTabFixture.getDDay();

		/* When */
		/* Then */
		assertThatCode(() -> {
			DDayWidget dDayWidget = DDayWidget.of(title, dDay);
			assertThat(dDayWidget.getTitle()).isEqualTo(title);
			assertThat(dDayWidget.getValue()).isEqualTo(dDay);
		}).doesNotThrowAnyException();
	}

	@DisplayName("D-Day 의 날짜 차이를 조회한다.")
	@ParameterizedTest
	@ValueSource(longs = {10L, -10L, 0L})
	void dDayBetweenTest(long days) {
		/* Given */
		DDayWidget dDayWidget = DDayWidget.of("디데이", LocalDate.now().plusDays(days));

		/* When */
		String dDay = dDayWidget.getDDay(CalendarTimeZone.KOREA.getTimeZone());

		/* Then */
		if (days > 0) {
			assertThat(dDay).isEqualTo("D-" + Math.abs(days));
		} else if (days < 0) {
			assertThat(dDay).isEqualTo("D+" + Math.abs(days));
		} else {
			assertThat(dDay).isEqualTo("D-DAY");
		}
	}

	@DisplayName("목표 Length Test")
	@Nested
	class MemoLengthTest {
		@DisplayName("D-Day 제목의 길이가 20이하이면 예외를 던지지 않는다.")
		@ParameterizedTest
		@ValueSource(ints = {0, 20})
		void generateMemoLength(int length) {
			/* Given */
			String dDayTitleAsString = "a".repeat(length);

			/* When */
			/* Then */
			assertThatCode(() -> {
				DDayWidget dDayWidget = DDayWidget.of(dDayTitleAsString, LocalDate.now());
				assertThat(dDayWidget.getTitle()).isEqualTo(dDayTitleAsString);
			}).doesNotThrowAnyException();
		}

		@DisplayName("D-Day 제목의 길이가 20초과이면 예외를 던진다.")
		@Test
		void validateTooLongContent() {
			/* Given */
			String dDayTitleAsString = "a".repeat(21);

			/* When */
			/* Then */
			assertThatThrownBy(() -> DDayWidget.of(dDayTitleAsString, null))
					.isInstanceOf(MemberException.class)
					.hasMessageContaining(MemberErrorCode.TOO_LONG_CONTENT.getMessage());
		}
	}

	@DisplayName("디데이 날짜 검증 테스트")
	@Nested
	class ValidateDateTest {
		@DisplayName("디데이 날짜 검증에 성공한다.")
		@Test
		void validateSuccessTest() {
			assertThatCode(() -> DDayWidget.of("디데이", LocalDate.now()))
					.doesNotThrowAnyException();
		}

		@DisplayName("1970년 이전이면 예외를 던진다.")
		@Test
		void before1970YearsTest() {
			assertThatThrownBy(() -> DDayWidget.of("디데이", LocalDate.of(1969, 12, 31)))
					.isInstanceOf(MemberException.class)
					.hasMessageContaining(MemberErrorCode.INVALID_DATE.getMessage());
		}

		@DisplayName("2200년 이후이면 예외를 던진다.")
		@Test
		void after2199YearsTest() {
			assertThatThrownBy(() -> DDayWidget.of("디데이", LocalDate.of(2200, 1, 1)))
					.isInstanceOf(MemberException.class)
					.hasMessageContaining(MemberErrorCode.INVALID_DATE.getMessage());
		}
	}
}