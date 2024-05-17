package com.butter.wypl.sidetab.domain.embedded;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.butter.wypl.member.exception.MemberErrorCode;
import com.butter.wypl.member.exception.MemberException;
import com.butter.wypl.sidetab.fixture.SideTabFixture;

class GoalWidgetTest {

	@DisplayName("목표 위젯 생성에 성공한다.")
	@ParameterizedTest
	@EnumSource(value = SideTabFixture.class)
	void generateGoal(SideTabFixture sideTabFixture) {
		/* Given */
		String goalAsString = sideTabFixture.getGoal();

		/* When */
		/* Then */
		assertThatCode(() -> {
			GoalWidget goal = GoalWidget.from(goalAsString);
			assertThat(goal.getValue()).isEqualTo(goalAsString);
		}).doesNotThrowAnyException();
	}

	@DisplayName("목표 Length Test")
	@Nested
	class MemoLengthTest {
		@DisplayName("목표의 길이가 60이하이면 예외를 던지지 않는다.")
		@ParameterizedTest
		@ValueSource(ints = {0, 60})
		void generateMemoLength(int length) {
			/* Given */
			String goalAsString = "a".repeat(length);

			/* When */
			/* Then */
			assertThatCode(() -> {
				GoalWidget goal = GoalWidget.from(goalAsString);
				assertThat(goal.getValue()).isEqualTo(goalAsString);
			}).doesNotThrowAnyException();
		}

		@DisplayName("목표의 길이가 60초과이면 예외를 던진다.")
		@Test
		void validateTooLongContent() {
			/* Given */
			String goalAsString = "a".repeat(61);

			/* When */
			/* Then */
			assertThatThrownBy(() -> GoalWidget.from(goalAsString))
					.isInstanceOf(MemberException.class)
					.hasMessageContaining(MemberErrorCode.TOO_LONG_CONTENT.getMessage());
		}
	}
}