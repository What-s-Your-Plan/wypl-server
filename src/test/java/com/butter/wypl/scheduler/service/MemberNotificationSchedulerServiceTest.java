package com.butter.wypl.scheduler.service;

import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.notification.service.ReviewNotificationService;
import com.butter.wypl.schedule.domain.MemberSchedule;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.fixture.ScheduleFixture;
import com.butter.wypl.schedule.respository.MemberScheduleRepository;

@MockServiceTest
class MemberNotificationSchedulerServiceTest {

	@InjectMocks
	private MemberNotificationSchedulerService memberNotificationSchedulerService;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	MemberScheduleRepository memberScheduleRepository;

	@Mock
	ReviewNotificationService reviewNotificationService;

	@Nested
	@DisplayName("dailyReviewScheduler 메서드 테스트")
	class DailyReviewSchedulerTest {

		@Test
		@DisplayName("성공")
		void whenSuccess() {
			/* Given */
			Member member1 = MemberFixture.HAN_JI_WON.toMemberWithId(1);
			Member member2 = MemberFixture.KIM_JEONG_UK.toMemberWithId(2);
			Member member3 = MemberFixture.JWA_SO_YEON.toMemberWithId(3);
			Schedule schedule1 = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.GROUP_SCHEDUEL.toSchedule();

			List<Member> allActiveMembers = List.of(member1, member2, member3);
			given(memberRepository.findAllActiveMembers()).willReturn(allActiveMembers);

			List<MemberSchedule> memberSchedules = List.of(
				MemberSchedule.of(member1, schedule1),
				MemberSchedule.of(member2, schedule2)
			);
			given(memberScheduleRepository.findMemberSchedulesEndingTodayWithoutReview(anyInt(), any(LocalDate.class)))
				.willReturn(memberSchedules);

			/* When, Then */
			Assertions.assertThatCode(() -> memberNotificationSchedulerService.runDailyReviewScheduler())
				.doesNotThrowAnyException();

		}
	}
}