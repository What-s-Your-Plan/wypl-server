package com.butter.wypl.schedule.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.group.repository.MemberGroupRepository;
import com.butter.wypl.label.domain.Label;
import com.butter.wypl.label.fixture.LabelFixture;
import com.butter.wypl.label.repository.LabelRepository;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.schedule.data.ModificationType;
import com.butter.wypl.schedule.data.request.RepetitionRequest;
import com.butter.wypl.schedule.data.request.ScheduleCreateRequest;
import com.butter.wypl.schedule.data.request.ScheduleUpdateRequest;
import com.butter.wypl.schedule.data.response.MemberIdResponse;
import com.butter.wypl.schedule.data.response.RepetitionResponse;
import com.butter.wypl.schedule.data.response.ScheduleDetailResponse;
import com.butter.wypl.schedule.data.response.ScheduleIdListResponse;
import com.butter.wypl.schedule.data.response.ScheduleResponse;
import com.butter.wypl.schedule.domain.Category;
import com.butter.wypl.schedule.domain.Repetition;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.fixture.ScheduleFixture;
import com.butter.wypl.schedule.fixture.embedded.RepetitionFixture;
import com.butter.wypl.schedule.respository.ScheduleRepository;

@MockServiceTest
public class ScheduleServiceTest {

	@InjectMocks
	private ScheduleServiceImpl scheduleService;

	@Mock
	private LabelRepository labelRepository;

	@Mock
	private MemberRepository memberRepository;
	@Mock
	private ScheduleRepository scheduleRepository;
	@Mock
	private MemberScheduleService memberScheduleService;

	@Mock
	private MemberGroupRepository memberGroupRepository;

	@Mock
	private RepetitionService repetitionService;

	//멤버 미리 생성
	private Member member1, member2;

	@BeforeEach
	void initLabel() {
		Label label = LabelFixture.STUDY_LABEL.toLabel();

		lenient().when(labelRepository.findByLabelId(anyInt())).thenReturn(Optional.of(label));
	}

	@BeforeEach
	void initMember() {
		member1 = MemberFixture.JWA_SO_YEON.toMember();
		member2 = MemberFixture.JO_DA_MIN.toMember();

		lenient().when(memberRepository.findById(1))
			.thenReturn(Optional.of(member1));
		lenient().when(memberRepository.findById(2))
			.thenReturn(Optional.of(member2));
	}

	@Nested
	@DisplayName("일정 등록")
	class CreateTest {

		@Test
		@DisplayName("반복 없는 개인 일정 등록이 정상적으로 이루어지는지 확인")
		void noRepeatPersonalSchedule() {

		}

		@Nested
		@DisplayName("반복")
		class RepeateCreateTest {
			@Test
			@DisplayName("주 반복")
			void weeklyRepeat() {

			}

			@Test
			@DisplayName("달 반복")
			void monthlyRepeat() {

			}

			@Test
			@DisplayName("년 반복")
			void yearlyRepeat() {

			}
		}

	}

	@Test
	@DisplayName("상세 일정 조회")
	void getDetailSchedule() {
		//given
		Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
		given(scheduleRepository.findById(anyInt()))
			.willReturn(Optional.of(schedule));

		//when
		ScheduleDetailResponse scheduleDetailResponse = scheduleService.getDetailScheduleByScheduleId(member1.getId(),
			schedule.getScheduleId());

		//then
		assertThat(scheduleDetailResponse.scheduleId()).isEqualTo(schedule.getScheduleId());
	}

	@Test
	@DisplayName("일정 조회")
	void getSchedule() {
		//given
		Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
		given(scheduleRepository.findById(anyInt()))
			.willReturn(Optional.of(schedule));

		//when
		ScheduleResponse scheduleResponse = scheduleService.getScheduleByScheduleId(member1.getId(),
			schedule.getScheduleId());

		//then
		assertThat(scheduleResponse.scheduleId()).isEqualTo(schedule.getScheduleId());
	}

	@Nested
	@DisplayName("일정 삭제")
	class DeleteTest {

		@Test
		@DisplayName("현재 일정만 삭제")
		void deleteNow() {

		}
	}

	@Nested
	@DisplayName("일정 수정")
	class UpdateTest {

		@Test
		@DisplayName("반복 없는 일정 -> 반복 없는 일정")
		void update1() {

		}

		@Test
		@DisplayName("반복 없는 일정 -> 반복 있는 일정")
		void update2() {

		}

		@Test
		@DisplayName("반복 있는 일정 -> 반복 없는 일정")
		void update3() {

		}

		@Test
		@DisplayName("반복 있는 일정 -> 반복 있는 일정")
		void update4() {

		}
	}
}
