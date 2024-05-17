package com.butter.wypl.calendar.service;

import static java.time.temporal.TemporalAdjusters.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.calendar.data.CalendarType;
import com.butter.wypl.calendar.data.response.BlockListResponse;
import com.butter.wypl.calendar.data.response.BlockResponse;
import com.butter.wypl.calendar.data.response.CalendarListResponse;
import com.butter.wypl.calendar.data.response.CalendarResponse;
import com.butter.wypl.calendar.data.response.GroupCalendarListResponse;
import com.butter.wypl.calendar.data.response.GroupCalendarResponse;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.repository.GroupRepository;
import com.butter.wypl.group.repository.MemberGroupRepository;
import com.butter.wypl.group.utils.GroupServiceUtils;
import com.butter.wypl.group.utils.MemberGroupServiceUtils;
import com.butter.wypl.label.domain.Label;
import com.butter.wypl.label.repository.LabelRepository;
import com.butter.wypl.label.utils.LabelServiceUtils;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.respository.MemberScheduleRepository;
import com.butter.wypl.schedule.respository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

	private final MemberScheduleRepository memberScheduleRepository;

	private final LabelRepository labelRepository;

	private final ScheduleRepository scheduleRepository;

	private final MemberGroupRepository memberGroupRepository;

	private final GroupRepository groupRepository;

	public CalendarListResponse getCalendarSchedules(int memberId, CalendarType calendarType, Integer labelId,
			LocalDate startDate) {
		if (startDate == null) {
			startDate = LocalDate.now();
		}

		LocalDate endDate = startDate;
		switch (calendarType) {
			case DAY -> {
			}
			case WEEK -> {
				startDate = startDate.with(WeekFields.of(Locale.KOREA).dayOfWeek(), 1);
				endDate = endDate.with(WeekFields.of(Locale.KOREA).dayOfWeek(), 7);
			}
			case MONTH -> {
				startDate = startDate.withDayOfMonth(1);
				endDate = endDate.with(TemporalAdjusters.lastDayOfMonth());
			}
		}

		List<Schedule> schedules;
		if (labelId == null) {
			schedules = memberScheduleRepository.getCalendarSchedules(memberId,
					LocalDateTime.of(startDate, LocalTime.of(0, 0)),
					LocalDateTime.of(endDate, LocalTime.of(23, 59)));
		} else {
			Label label = LabelServiceUtils.getLabelByLabelId(labelRepository, labelId);

			schedules = memberScheduleRepository.getCalendarSchedulesWithLabel(memberId,
					LocalDateTime.of(startDate, LocalTime.of(0, 0)),
					LocalDateTime.of(endDate, LocalTime.of(23, 59)),
					label.getLabelId());
		}

		return CalendarListResponse.from(
				schedules.stream().map(
						schedule -> CalendarResponse.of(schedule,
								(schedule.getGroupId() == null) ? null :
										MemberGroupServiceUtils.getAcceptMemberGroup(memberGroupRepository, memberId,
												schedule.getGroupId()))
				).toList()
		);
	}

	public GroupCalendarListResponse getGroupCalendarSchedule(int memberId, CalendarType calendarType,
			LocalDate startDate, int groupId) {
		//그룹에 속한 멤버인지 확인
		//그룹의 존재 여부 확인
		MemberGroup memberGroup = MemberGroupServiceUtils.getAcceptMemberGroup(memberGroupRepository, memberId,
				GroupServiceUtils.findById(groupRepository, groupId).getId());

		if (startDate == null) {
			startDate = LocalDate.now();
		}

		LocalDate endDate = startDate;
		switch (calendarType) {
			case DAY -> {
			}
			case WEEK -> {
				startDate = startDate.with(WeekFields.of(Locale.KOREA).dayOfWeek(), 1);
				endDate = endDate.with(WeekFields.of(Locale.KOREA).dayOfWeek(), 7);
			}
			case MONTH -> {
				startDate = startDate.withDayOfMonth(1);
				endDate = endDate.with(TemporalAdjusters.lastDayOfMonth());
			}
		}

		List<Schedule> schedules = scheduleRepository.findAllByGroupIdAndStartDateBetween(groupId,
				LocalDateTime.of(startDate, LocalTime.of(0, 0)),
				LocalDateTime.of(endDate, LocalTime.of(23, 59)));

		return GroupCalendarListResponse.of(
				schedules.stream()
						.map(schedule -> GroupCalendarResponse.of(schedule,
								memberScheduleRepository.getMemberWithSchedule(schedule.getScheduleId())))
						.toList(),
				memberGroup
		);
	}

	public BlockListResponse getVisualization(int memberId, LocalDate startDate) {
		if (startDate == null) {
			startDate = LocalDate.now();
		}

		startDate = startDate.with(firstDayOfYear());
		LocalDate endDate = startDate.with(lastDayOfYear());

		HashMap<LocalDate, Long> yearBlocks = new HashMap<>();

		List<Schedule> schedules = memberScheduleRepository.getCalendarSchedules(memberId,
				LocalDateTime.of(startDate, LocalTime.of(0, 0)),
				LocalDateTime.of(endDate, LocalTime.of(0, 0)));

		for (Schedule schedule : schedules) {
			LocalDate scheduleStartDate = schedule.getStartDate().toLocalDate();
			LocalDate scheduleEndDate = schedule.getEndDate().toLocalDate();

			for (LocalDate date = scheduleStartDate;
				 date.isBefore(scheduleEndDate) || date.isEqual(scheduleEndDate); date = date.plusDays(1)) {

				long diffTime = 0L;

				if (date.isEqual(scheduleStartDate) && date.isEqual(scheduleEndDate)) {
					diffTime = Duration.between(schedule.getStartDate(), schedule.getEndDate()).toMinutes();
				} else if (date.isEqual(scheduleStartDate)) {
					diffTime = Duration.between(schedule.getStartDate(),
							LocalDateTime.of(scheduleStartDate, LocalTime.of(23, 59))).toMinutes();
				} else if (date.isEqual(scheduleEndDate)) {
					diffTime = Duration.between(
							LocalDateTime.of(scheduleEndDate, LocalTime.of(0, 0)), schedule.getEndDate()).toMinutes();
				} else {
					diffTime = 24 * 60;
				}

				if (yearBlocks.containsKey(date)) {
					Long totalTime = yearBlocks.get(date);

					yearBlocks.put(date, totalTime + diffTime);
				} else {
					yearBlocks.put(date, diffTime);
				}
			}
		}

		List<BlockResponse> blockResponses = new ArrayList<>();
		for (LocalDate date = startDate; date.isEqual(endDate) || date.isBefore(endDate); date = date.plusDays(1)) {
			if (yearBlocks.containsKey(date)) {
				blockResponses.add(new BlockResponse(date, yearBlocks.get(date)));

				continue;
			}

			blockResponses.add(new BlockResponse(date, 0));
		}

		return BlockListResponse.from(blockResponses);
	}
}
