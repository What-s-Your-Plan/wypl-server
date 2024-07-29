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
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.fixture.ScheduleFixture;

@MockServiceTest
class MemberNotificationSchedulerServiceTest {

	@InjectMocks
	private MemberNotificationSchedulerService memberNotificationSchedulerService;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	ReviewNotificationService reviewNotificationService;

	@Nested
	@DisplayName("dailyReviewScheduler 메서드 테스트")
	class DailyReviewSchedulerTest {

		@Test
		@DisplayName("성공")
		void whenSuccess() {

		}
	}
}