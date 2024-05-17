package com.butter.wypl.group.domain;

import static com.butter.wypl.group.exception.GroupErrorCode.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.butter.wypl.group.exception.GroupException;

class GroupTest {

	@Nested
	@DisplayName("그룹 이름 검증")
	class validateNameTest {

		@Test
		@DisplayName("성공")
		void whenSuccess() {
			/* Given */
			String name = "주말 수영 모임";

			/* When, Then */
			assertThatCode(() -> {
				Group.validateName(name);
			}).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("그룹 이름이 20자 초과이면 실패")
		void whenFailExceedsMaxSize() {

			/* Given */
			String name = "너무긴그룹이름".repeat(10);

			/* When, Then */
			assertThatCode(() -> {
				Group.validateName("123456789012345678901");
			}).isInstanceOf(GroupException.class)
				.hasMessageContaining(NOT_APPROPRIATE_TYPE_OF_GROUP_NAME.getMessage());

		}

		@Test
		@DisplayName("그룹 이름이 null이면 실패")
		void whenFailNull() {

			/* Given */
			String name = null;

			/* When, Then */
			assertThatCode(() -> {
				Group.validateName(name);
			}).isInstanceOf(GroupException.class)
				.hasMessageContaining(NOT_APPROPRIATE_TYPE_OF_GROUP_NAME.getMessage());
		}

		@Test
		@DisplayName("그룹 이름이 공백이면 실패")
		void whenFailBlank() {

			/* Given */
			String name = "  ";

			/* When, Then */
			assertThatCode(() -> {
				Group.validateName(name);
			}).isInstanceOf(GroupException.class)
				.hasMessageContaining(NOT_APPROPRIATE_TYPE_OF_GROUP_NAME.getMessage());
		}
	}
}