package com.butter.wypl.calendar.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.calendar.data.CalendarType;
import com.butter.wypl.calendar.data.response.CalendarListResponse;
import com.butter.wypl.calendar.data.response.GroupCalendarListResponse;
import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.group.domain.Group;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.fixture.GroupFixture;
import com.butter.wypl.group.repository.GroupRepository;
import com.butter.wypl.group.repository.MemberGroupRepository;
import com.butter.wypl.label.domain.Label;
import com.butter.wypl.label.fixture.LabelFixture;
import com.butter.wypl.label.repository.LabelRepository;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.fixture.ScheduleFixture;
import com.butter.wypl.schedule.respository.MemberScheduleRepository;
import com.butter.wypl.schedule.respository.ScheduleRepository;

@MockServiceTest
public class CalendarServiceTest {

	@InjectMocks
	private CalendarService calendarService;

	@Mock
	private MemberScheduleRepository memberScheduleRepository;

	@Mock
	private ScheduleRepository scheduleRepository;

	@Mock
	private LabelRepository labelRepository;

	@Mock
	private GroupRepository groupRepository;

	@Mock
	private MemberGroupRepository memberGroupRepository;

	@Nested
	@DisplayName("개인 페이지 캘린더 조회")
	class getPersonalCalendar {

		@BeforeEach
		void init() {
			Member member = MemberFixture.JWA_SO_YEON.toMember();
			Group group = GroupFixture.GROUP_STUDY.toGroup(member);

			lenient().when(memberGroupRepository.findAcceptMemberGroup(anyInt(), anyInt()))
				.thenReturn(Optional.ofNullable(MemberGroup.of(member, group, Color.labelBlue)));
		}

		@Test
		@DisplayName("기준일이 설정안된 calendar 조회 - 하루")
		void getCalendar() {
			// Given
			Schedule schedule1 = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();

			given(memberScheduleRepository.getCalendarSchedules(anyInt(), any(LocalDateTime.class),
				any(LocalDateTime.class)))
				.willReturn(
					List.of(schedule1, schedule2)
				);

			// When
			CalendarListResponse calendarListResponse = calendarService.getCalendarSchedules(1, CalendarType.DAY, null,
				null);

			// Then
			assertThat(calendarListResponse.scheduleCount()).isEqualTo(2);
		}

		@Test
		@DisplayName("기준일이 설정된 calendar 조회- 하루")
		void getStandardCalendar() {
			// Given
			Schedule schedule1 = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();

			given(memberScheduleRepository.getCalendarSchedules(anyInt(), any(LocalDateTime.class),
				any(LocalDateTime.class)))
				.willReturn(
					List.of(schedule1, schedule2)
				);

			// When
			CalendarListResponse calendarListResponse = calendarService.getCalendarSchedules(1, CalendarType.DAY, null,
				LocalDate.of(2024, 5, 8));

			// Then
			assertThat(calendarListResponse.scheduleCount()).isEqualTo(2);

		}

		@Test
		@DisplayName("라벨이 있는 기준일 설정 안된 calendar 조회- 하루")
		void getLabelCalendar() {
			// Given
			Schedule schedule1 = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();

			Label label = LabelFixture.STUDY_LABEL.toLabel();

			given(labelRepository.findByLabelId(anyInt())).willReturn(Optional.of(label));

			given(memberScheduleRepository.getCalendarSchedulesWithLabel(anyInt(), any(LocalDateTime.class),
				any(LocalDateTime.class), anyInt()))
				.willReturn(
					List.of(schedule1, schedule2)
				);

			// When
			CalendarListResponse calendarListResponse = calendarService.getCalendarSchedules(1, CalendarType.DAY,
				label.getLabelId(), null);

			// Then
			assertThat(calendarListResponse.scheduleCount()).isEqualTo(2);

		}

		@Test
		@DisplayName("라벨이 있는 기준일 설정된 calendar 조회- 하루")
		void getLabelStandardCalendar() {
			// Given
			Schedule schedule1 = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();

			Label label = LabelFixture.STUDY_LABEL.toLabel();

			given(labelRepository.findByLabelId(anyInt())).willReturn(Optional.of(label));

			given(memberScheduleRepository.getCalendarSchedulesWithLabel(anyInt(), any(LocalDateTime.class),
				any(LocalDateTime.class), anyInt()))
				.willReturn(
					List.of(schedule1, schedule2)
				);

			// When
			CalendarListResponse calendarListResponse = calendarService.getCalendarSchedules(1, CalendarType.DAY,
				label.getLabelId(), LocalDate.of(2024, 5, 8));

			// Then
			assertThat(calendarListResponse.scheduleCount()).isEqualTo(2);
		}

		@Test
		@DisplayName("일주일 조회")
		void getWeekCalendar() {
			// Given
			Schedule schedule1 = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();

			given(memberScheduleRepository.getCalendarSchedules(anyInt(), any(LocalDateTime.class),
				any(LocalDateTime.class)))
				.willReturn(
					List.of(schedule1, schedule2)
				);

			// When
			CalendarListResponse calendarListResponse = calendarService.getCalendarSchedules(1, CalendarType.WEEK,
				null,
				null);

			// Then
			assertThat(calendarListResponse.scheduleCount()).isEqualTo(2);
		}

		@Test
		@DisplayName("달 조회")
		void getMonthCalendar() {
			// Given
			Schedule schedule1 = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();

			given(memberScheduleRepository.getCalendarSchedules(anyInt(), any(LocalDateTime.class),
				any(LocalDateTime.class)))
				.willReturn(
					List.of(schedule1, schedule2)
				);

			// When
			CalendarListResponse calendarListResponse = calendarService.getCalendarSchedules(1, CalendarType.MONTH,
				null,
				null);

			// Then
			assertThat(calendarListResponse.scheduleCount()).isEqualTo(2);

		}
	}

	@Nested
	@DisplayName("그룹 페이지 캘린더 조회")
	class getGroupCalendar {

		@Test
		@DisplayName("하루 조회")
		void getDay() {
			// Given
			Schedule schedule1 = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();

			Member member = MemberFixture.KIM_JEONG_UK.toMember();
			Group group = GroupFixture.GROUP_STUDY.toGroup(member);

			given(scheduleRepository.findAllByGroupIdAndStartDateBetween(anyInt(), any(LocalDateTime.class),
				any(LocalDateTime.class)))
				.willReturn(
					List.of(schedule1, schedule2)
				);

			given(groupRepository.findById(anyInt()))
				.willReturn(Optional.of(group));

			given(memberGroupRepository.findAcceptMemberGroup(anyInt(), anyInt()))
				.willReturn(Optional.ofNullable(MemberGroup.of(member, group, Color.labelBlue)));

			// When
			GroupCalendarListResponse groupCalendarListResponse = calendarService.getGroupCalendarSchedule(1,
				CalendarType.DAY,
				null,
				1);

			// Then
			assertThat(groupCalendarListResponse.scheduleCount()).isEqualTo(2);

		}

		@Test
		@DisplayName("주 조회")
		void getWeek() {
			// Given
			Schedule schedule1 = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();

			Member member = MemberFixture.KIM_JEONG_UK.toMember();
			Group group = GroupFixture.GROUP_STUDY.toGroup(member);

			given(scheduleRepository.findAllByGroupIdAndStartDateBetween(anyInt(), any(LocalDateTime.class),
				any(LocalDateTime.class)))
				.willReturn(
					List.of(schedule1, schedule2)
				);

			given(groupRepository.findById(anyInt()))
				.willReturn(Optional.of(group));

			given(memberGroupRepository.findAcceptMemberGroup(anyInt(), anyInt()))
				.willReturn(Optional.ofNullable(MemberGroup.of(member, group, Color.labelBlue)));

			// When
			GroupCalendarListResponse groupCalendarListResponse = calendarService.getGroupCalendarSchedule(1,
				CalendarType.WEEK,
				null,
				1);

			// Then
			assertThat(groupCalendarListResponse.scheduleCount()).isEqualTo(2);

		}

		@Test
		@DisplayName("달 조회")
		void getMonth() {
			// Given
			Schedule schedule1 = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();

			Member member = MemberFixture.KIM_JEONG_UK.toMember();
			Group group = GroupFixture.GROUP_STUDY.toGroup(member);

			given(scheduleRepository.findAllByGroupIdAndStartDateBetween(anyInt(), any(LocalDateTime.class),
				any(LocalDateTime.class)))
				.willReturn(
					List.of(schedule1, schedule2)
				);

			given(groupRepository.findById(anyInt()))
				.willReturn(Optional.of(group));

			given(memberGroupRepository.findAcceptMemberGroup(anyInt(), anyInt()))
				.willReturn(Optional.ofNullable(MemberGroup.of(member, group, Color.labelBlue)));

			// When
			GroupCalendarListResponse groupCalendarListResponse = calendarService.getGroupCalendarSchedule(1,
				CalendarType.MONTH,
				null,
				1);

			// Then
			assertThat(groupCalendarListResponse.scheduleCount()).isEqualTo(2);

		}

		@Test
		@DisplayName("기준일 설정된 경우 - 하루")
		void getStandard() {
			// Given
			Schedule schedule1 = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();

			Member member = MemberFixture.KIM_JEONG_UK.toMember();
			Group group = GroupFixture.GROUP_STUDY.toGroup(member);

			given(scheduleRepository.findAllByGroupIdAndStartDateBetween(anyInt(), any(LocalDateTime.class),
				any(LocalDateTime.class)))
				.willReturn(
					List.of(schedule1, schedule2)
				);

			given(groupRepository.findById(anyInt()))
				.willReturn(Optional.of(group));

			given(memberGroupRepository.findAcceptMemberGroup(anyInt(), anyInt()))
				.willReturn(Optional.ofNullable(MemberGroup.of(member, group, Color.labelBlue)));

			// When
			GroupCalendarListResponse groupCalendarListResponse = calendarService.getGroupCalendarSchedule(1,
				CalendarType.DAY,
				LocalDate.of(2024, 5, 8),
				1);

			// Then
			assertThat(groupCalendarListResponse.scheduleCount()).isEqualTo(2);

		}
	}

	@Test
	@DisplayName("시각화(년 캘린더) 조회")
	void getYearCalendar() {
		// Given
		Schedule schedule1 = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
		Schedule schedule2 = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();

		given(memberScheduleRepository.getCalendarSchedules(anyInt(), any(LocalDateTime.class),
			any(LocalDateTime.class)))
			.willReturn(
				List.of(schedule1, schedule2)
			);

		//when
		//then
		assertThatCode(() -> {
			calendarService.getVisualization(1, LocalDate.now());
		}).doesNotThrowAnyException();
	}
}
