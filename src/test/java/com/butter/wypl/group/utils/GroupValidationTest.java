package com.butter.wypl.group.utils;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;

class GroupValidationTest {

	@Nested
	@DisplayName("그룹 맴버 유효성 검사")
	class ValidateCreateGroup {
		private List<Member> groupMembers;

		@BeforeEach
		void setUp() {
			MemberFixture[] fixtures = MemberFixture.values();
			groupMembers = IntStream.rangeClosed(1, fixtures.length)
				.mapToObj(i -> fixtures[i - 1].toMemberWithId(i))
				.toList();

		}

		@Test
		void whenSuccess() {

			/* Given */
			int userId = groupMembers.size() - 1;

			/* When, Then */
			assertThatCode(() -> GroupServiceUtils.isGroupMember(userId, groupMembers)).doesNotThrowAnyException();

		}

		@Test
		void whenFail() {

			/* Given */
			int userId = groupMembers.size();

			/* When, Then */
			assertThatCode(() -> GroupServiceUtils.isGroupMember(userId, groupMembers)).doesNotThrowAnyException();

		}

	}

}