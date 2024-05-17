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
import com.butter.wypl.schedule.domain.MemberSchedule;
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
			//given
			Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			given(scheduleRepository.save(any()))
				.willReturn(schedule);

			//when
			ScheduleDetailResponse result = scheduleService.createSchedule(1,
				ScheduleCreateRequest.of(schedule, List.of(new MemberIdResponse(1)))
			);

			//then
			assertThat(result).isNotNull();
			assertThat(result.groupId()).isNull();
			assertThat(result.title()).isEqualTo(schedule.getTitle());
			assertThat(result.startDate()).isEqualTo(schedule.getStartDate());
			assertThat(result.endDate()).isEqualTo(schedule.getEndDate());
			assertThat(result.category()).isEqualTo(Category.MEMBER);
			assertThat(result.labelId()).isNull();
			assertThat(result.repetition()).isNull();
		}

		@Nested
		@DisplayName("반복")
		class RepeateCreateTest {
			@Test
			@DisplayName("주 반복")
			void weeklyRepeat() {
				//given
				Repetition repetition = RepetitionFixture.MONDAY_REPETITION.toRepetition();
				given(repetitionService.createRepetition(any())).willReturn(repetition);

				Schedule schedule = ScheduleFixture.LABEL_REPEAT_PERSONAL_SCHEDULE.toSchedule();
				given(scheduleRepository.save(any()))
					.willReturn(schedule);

				//when
				ScheduleDetailResponse result = scheduleService.createSchedule(1,
					ScheduleCreateRequest.of(schedule, List.of(new MemberIdResponse(1))));

				//then
				assertThat(result).isNotNull();
				assertThat(result.groupId()).isNull();
				assertThat(result.title()).isEqualTo(schedule.getTitle());
				assertThat(result.startDate()).isEqualTo(schedule.getStartDate());
				assertThat(result.endDate()).isEqualTo(schedule.getEndDate());
				assertThat(result.category()).isEqualTo(Category.MEMBER);
				assertThat(result.labelId()).isNotNull();
				assertThat(result.repetition()).isEqualTo(RepetitionResponse.from(schedule.getRepetition()));
			}

			@Test
			@DisplayName("달 반복")
			void monthlyRepeat() {
				//given
				Repetition repetition = RepetitionFixture.MONTHLY_REPETITION.toRepetition();
				given(repetitionService.createRepetition(any())).willReturn(repetition);

				Schedule schedule = ScheduleFixture.REPEAT_PERSONAL_SCHEDULE.toSchedule();
				given(scheduleRepository.save(any()))
					.willReturn(schedule);

				//when
				ScheduleDetailResponse result = scheduleService.createSchedule(1,
					ScheduleCreateRequest.of(schedule, List.of(new MemberIdResponse(1))));

				//then
				assertThat(result).isNotNull();
				assertThat(result.groupId()).isNull();
				assertThat(result.title()).isEqualTo(schedule.getTitle());
				assertThat(result.startDate()).isEqualTo(schedule.getStartDate());
				assertThat(result.endDate()).isEqualTo(schedule.getEndDate());
				assertThat(result.category()).isEqualTo(Category.MEMBER);
				assertThat(result.labelId()).isNull();
				assertThat(result.repetition()).isEqualTo(RepetitionResponse.from(schedule.getRepetition()));
			}

			@Test
			@DisplayName("년 반복")
			void yearlyRepeat() {
				//given
				Repetition repetition = RepetitionFixture.YEARLY_REPETITION.toRepetition();
				given(repetitionService.createRepetition(any())).willReturn(repetition);

				Schedule schedule = ScheduleFixture.REPEAT_GROUP_SCHEDULE.toSchedule();
				given(scheduleRepository.save(any()))
					.willReturn(schedule);
				Member member = MemberFixture.KIM_JEONG_UK.toMember();

				//when
				ScheduleDetailResponse result = scheduleService.createSchedule(1,
					ScheduleCreateRequest.of(schedule, List.of(new MemberIdResponse(1))));

				//then
				assertThat(result).isNotNull();
				assertThat(result.groupId()).isNotNull();
				assertThat(result.title()).isEqualTo(schedule.getTitle());
				assertThat(result.startDate()).isEqualTo(schedule.getStartDate());
				assertThat(result.endDate()).isEqualTo(schedule.getEndDate());
				assertThat(result.category()).isEqualTo(Category.GROUP);
				assertThat(result.labelId()).isNull();
				assertThat(result.repetition()).isEqualTo(RepetitionResponse.from(schedule.getRepetition()));

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
			// Given
			Schedule schedule = ScheduleFixture.REPEAT_PERSONAL_SCHEDULE.toSchedule();
			given(scheduleRepository.findById(anyInt())).willReturn(Optional.of(schedule));
			given(memberScheduleService.getMemberScheduleByMemberAndSchedule(anyInt(), any(Schedule.class)))
				.willReturn(MemberSchedule.builder().member(member1).schedule(schedule).build());
			// When
			ScheduleIdListResponse scheduleIdListResponse = scheduleService.deleteSchedule(
				1, 1, ModificationType.NOW
			);
			// Then
			assertThat(scheduleIdListResponse.scheduleCount()).isEqualTo(1);
		}
	}

	@Nested
	@DisplayName("일정 수정")
	class UpdateTest {

		@Test
		@DisplayName("반복 없는 일정 -> 반복 없는 일정")
		void update1() {
			// Given
			Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			given(scheduleRepository.findById(anyInt())).willReturn(Optional.of(schedule));

			// When
			ScheduleDetailResponse updateSchedule = scheduleService.updateSchedule(1, 1,
				new ScheduleUpdateRequest(
					"바뀐 제목",
					"바뀐 설명",
					schedule.getStartDate(),
					schedule.getEndDate(),
					ModificationType.NOW,
					null,
					1,
					List.of(new MemberIdResponse(1))
				));

			// Then
			assertThat(updateSchedule).isNotNull();
			assertThat(updateSchedule.title()).isEqualTo("바뀐 제목");
		}

		@Test
		@DisplayName("반복 없는 일정 -> 반복 있는 일정")
		void update2() {
			// Given
			Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			given(scheduleRepository.findById(anyInt())).willReturn(Optional.of(schedule));
			given(repetitionService.createRepetition(any(Repetition.class)))
				.willReturn(
					RepetitionFixture.MONDAY_REPETITION.toRepetition()
				);

			// When
			ScheduleDetailResponse updateSchedule = scheduleService.updateSchedule(1, 1,
				new ScheduleUpdateRequest(
					"바뀐 제목",
					"바뀐 설명",
					schedule.getStartDate(),
					schedule.getEndDate(),
					ModificationType.NOW,
					RepetitionRequest.from(RepetitionFixture.MONDAY_REPETITION.toRepetition()),
					1,
					List.of(new MemberIdResponse(1))
				));

			//then
			assertThat(updateSchedule).isNotNull();
			assertThat(updateSchedule.title()).isEqualTo("바뀐 제목");
			assertThat(updateSchedule.repetition().repetitionStartDate()).isEqualTo(
				RepetitionFixture.MONDAY_REPETITION.toRepetition().getRepetitionStartDate());
		}

		@Test
		@DisplayName("반복 있는 일정 -> 반복 없는 일정")
		void update3() {
			// Given
			Schedule schedule = ScheduleFixture.REPEAT_PERSONAL_SCHEDULE.toSchedule();
			given(scheduleRepository.findById(anyInt())).willReturn(Optional.of(schedule));

			// When
			ScheduleDetailResponse updateSchedule = scheduleService.updateSchedule(1, 1,
				new ScheduleUpdateRequest(
					"바뀐 제목",
					"바뀐 설명",
					schedule.getStartDate(),
					schedule.getEndDate(),
					ModificationType.NOW,
					null,
					1,
					List.of(new MemberIdResponse(1))
				));

			// Then
			assertThat(updateSchedule).isNotNull();
			assertThat(updateSchedule.title()).isEqualTo("바뀐 제목");
			assertThat(updateSchedule.repetition()).isNull();
		}

		@Test
		@DisplayName("반복 있는 일정 -> 반복 있는 일정")
		void update4() {
			// Given
			Schedule schedule = ScheduleFixture.REPEAT_PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			given(scheduleRepository.findById(anyInt())).willReturn(Optional.of(schedule));
			given(scheduleRepository.findAllByRepetitionAndStartDateAfter(any(Repetition.class),
				any(LocalDateTime.class)))
				.willReturn(
					List.of(schedule2)
				);
			given(repetitionService.createRepetition(any(Repetition.class)))
				.willReturn(
					RepetitionFixture.MONDAY_REPETITION.toRepetition()
				);
			given(memberScheduleService.getMemberScheduleByMemberAndSchedule(anyInt(), any(Schedule.class)))
				.willReturn(MemberSchedule.builder().member(member1).schedule(schedule).build());
			// When
			ScheduleDetailResponse updateSchedule = scheduleService.updateSchedule(1, 1,
				new ScheduleUpdateRequest(
					"바뀐 제목",
					"바뀐 설명",
					schedule.getStartDate(),
					schedule.getEndDate(),
					ModificationType.AFTER,
					RepetitionRequest.from(RepetitionFixture.MONDAY_REPETITION.toRepetition()),
					1,
					List.of(new MemberIdResponse(1))
				));

			//then
			assertThat(updateSchedule).isNotNull();
			assertThat(updateSchedule.title()).isEqualTo("바뀐 제목");
			assertThat(updateSchedule.repetition().repetitionStartDate()).isEqualTo(
				RepetitionFixture.MONDAY_REPETITION.toRepetition().getRepetitionStartDate());

		}
	}
}
