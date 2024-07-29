package com.butter.wypl.schedule.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.schedule.data.response.MemberIdResponse;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.exception.ScheduleErrorCode;
import com.butter.wypl.schedule.exception.ScheduleException;
import com.butter.wypl.schedule.respository.ScheduleRepository;

@MockServiceTest
public class MemberScheduleServiceTest {
	@InjectMocks
	private MemberScheduleService memberScheduleService;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private ScheduleRepository scheduleRepository;

	private Schedule schedule;

	@Test
	@DisplayName("멤버-일정 조회")
	void getMemberBySchedule() {

	}

	@Nested
	@DisplayName("생성")
	class CreateTest {
		@DisplayName("멤버-일정 생성")
		@Test
		void createMemberSchedule() {

		}
	}

	@Nested
	@DisplayName("유효성 검사")
	class ValidateTest {

		@Test
		@DisplayName("유효성 검사 정상 동작")
		void validateMemberSchedule() {

		}

		@Test
		@DisplayName("유효성 검사 실패")
		void validateMemberScheduleException() {
			// Given
			Member member = MemberFixture.JWA_SO_YEON.toMember();
			given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(member));

			// When
			// Then
			assertThatThrownBy(() -> {
				memberScheduleService.validateMemberSchedule(schedule, member.getId());
			}).isInstanceOf(ScheduleException.class)
				.hasMessageContaining(ScheduleErrorCode.NOT_PERMISSION_TO_SCHEDUEL.getMessage());
		}
	}

	@Nested
	@DisplayName("멤버-일정 수정")
	class update {

		@Test
		@DisplayName("수정이 정상적으로 동작")
		void updateMemberSchedule() {

		}
	}
}
