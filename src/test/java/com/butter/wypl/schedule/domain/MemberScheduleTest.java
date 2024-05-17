package com.butter.wypl.schedule.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.schedule.fixture.ScheduleFixture;

class MemberScheduleTest {
	@DisplayName("일정에 리뷰가 있으면 true를 반환한다.")
	@Test
	void writeReviewTest() {
		/* Given */
		MemberSchedule memberSchedule = MemberSchedule.builder()
				.member(MemberFixture.KIM_JEONG_UK.toMember())
				.schedule(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule())
				.build();

		/* When */
		memberSchedule.writeReview();

		/* Then */
		assertThat(memberSchedule.isWriteReview()).isTrue();
	}
}