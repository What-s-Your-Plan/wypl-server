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

class MemoWidgetTest {

	@DisplayName("메모 위젯 생성에 성공한다.")
	@ParameterizedTest
	@EnumSource(value = SideTabFixture.class)
	void generateMemo(SideTabFixture sideTabFixture) {
		/* Given */
		String memoAsString = sideTabFixture.getMemo();

		/* When */
		/* Then */
		assertThatCode(() -> {
			MemoWidget memo = MemoWidget.from(memoAsString);
			assertThat(memo.getValue()).isEqualTo(memoAsString);
		}).doesNotThrowAnyException();
	}

	@DisplayName("메모 Length Test")
	@Nested
	class MemoLengthTest {
		@DisplayName("메모의 길이가 1_000미만이면 예외를 던지지 않는다.")
		@ParameterizedTest
		@ValueSource(ints = {0, 1_000})
		void generateMemoLength(int length) {
			/* Given */
			String memoAsString = "a".repeat(length);

			/* When */
			/* Then */
			assertThatCode(() -> {
				MemoWidget memo = MemoWidget.from(memoAsString);
				assertThat(memo.getValue()).isEqualTo(memoAsString);
			}).doesNotThrowAnyException();
		}

		@DisplayName("메모의 길이가 1_000초과이면 예외를 던진다.")
		@Test
		void validateTooLongContent() {
			/* Given */
			String memoAsString = "a".repeat(1_001);

			/* When */
			/* Then */
			assertThatThrownBy(() -> MemoWidget.from(memoAsString))
					.isInstanceOf(MemberException.class)
					.hasMessageContaining(MemberErrorCode.TOO_LONG_CONTENT.getMessage());
		}
	}
}