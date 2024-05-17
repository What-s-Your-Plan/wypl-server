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
import com.butter.wypl.schedule.domain.MemberSchedule;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.exception.ScheduleErrorCode;
import com.butter.wypl.schedule.exception.ScheduleException;
import com.butter.wypl.schedule.respository.MemberScheduleRepository;
import com.butter.wypl.schedule.respository.ScheduleRepository;

@MockServiceTest
public class MemberScheduleServiceTest {
	@InjectMocks
	private MemberScheduleService memberScheduleService;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private MemberScheduleRepository memberScheduleRepository;

	@Mock
	private ScheduleRepository scheduleRepository;

	private Schedule schedule;

	@Test
	@DisplayName("멤버-일정 조회")
	void getMemberBySchedule() {
		// Given
		Member member1 = MemberFixture.JWA_SO_YEON.toMember();
		Member member2 = MemberFixture.KIM_JEONG_UK.toMember();

		given(memberScheduleRepository.findAllBySchedule(schedule))
			.willReturn(List.of(
				MemberSchedule.builder()
					.schedule(schedule)
					.member(member1)
					.build(),
				MemberSchedule.builder()
					.schedule(schedule)
					.member(member2)
					.build()
			));

		// When
		List<Member> members = memberScheduleService.getMembersBySchedule(schedule);

		// Then
		assertThat(members.size()).isEqualTo(2);

	}

	@Nested
	@DisplayName("생성")
	class CreateTest {
		@DisplayName("멤버-일정 생성")
		@Test
		void createMemberSchedule() {
			//given
			Member member1 = MemberFixture.JWA_SO_YEON.toMember();
			Member member2 = MemberFixture.JO_DA_MIN.toMember();

			List<MemberIdResponse> memberIdResponses = new ArrayList<>();
			memberIdResponses.add(new MemberIdResponse(1));
			memberIdResponses.add(new MemberIdResponse(2));

			given(memberRepository.findById(1))
				.willReturn(Optional.of(member1));
			given(memberRepository.findById(2))
				.willReturn(Optional.of(member2));

			given(memberScheduleRepository.saveAll(Mockito.anyList()))
				.willReturn(List.of(
					MemberSchedule.builder()
						.member(member1)
						.schedule(schedule)
						.build(),
					MemberSchedule.builder()
						.member(member2)
						.schedule(schedule)
						.build()
				));

			//when
			List<Member> members =
				memberScheduleService.createMemberSchedule(schedule, memberIdResponses);

			//then
			assertThat(members.size()).isEqualTo(2);
			assertThat(members.get(0).getNickname()).isEqualTo(member1.getNickname());
		}
	}

	@Nested
	@DisplayName("유효성 검사")
	class ValidateTest {

		@Test
		@DisplayName("유효성 검사 정상 동작")
		void validateMemberSchedule() {
			// Given
			Member member = MemberFixture.JWA_SO_YEON.toMember();
			given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(member));

			given(memberScheduleRepository.findByScheduleAndMember(any(), any()))
				.willReturn(
					Optional.ofNullable(MemberSchedule.builder()
						.member(member)
						.schedule(schedule)
						.build()));

			// When
			// Then
			assertThatCode(() -> {
				memberScheduleService.validateMemberSchedule(schedule, member.getId());
			}).doesNotThrowAnyException();

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
			//given
			Member member1 = MemberFixture.JWA_SO_YEON.toMemberWithId(1);
			Member member2 = MemberFixture.JO_DA_MIN.toMemberWithId(2);

			List<MemberIdResponse> memberIdResponses = new ArrayList<>();
			memberIdResponses.add(new MemberIdResponse(1));

			given(memberRepository.findById(1))
				.willReturn(Optional.of(member1));
			given(memberRepository.findById(2))
				.willReturn(Optional.of(member2));

			given(memberScheduleRepository.saveAll(any()))
				.willReturn(List.of(
					MemberSchedule.builder()
						.member(member1)
						.schedule(schedule)
						.build()
				));

			given(memberScheduleRepository.findAllBySchedule(schedule))
				.willReturn(List.of(
					MemberSchedule.builder()
						.member(member1)
						.schedule(schedule)
						.build()
				));

			memberScheduleService.createMemberSchedule(schedule, memberIdResponses);

			given(memberScheduleRepository.save(any()))
				.willReturn(MemberSchedule.builder()
					.member(member2)
					.schedule(schedule)
					.build());
			//when
			List<Member> updatedMembers = memberScheduleService.updateMemberSchedule(schedule,
				List.of(new MemberIdResponse(2)));

			//then
			assertThat(updatedMembers.size()).isEqualTo(1);
			assertThat(updatedMembers.get(0).getNickname()).isEqualTo(member2.getNickname());
		}
	}
}
