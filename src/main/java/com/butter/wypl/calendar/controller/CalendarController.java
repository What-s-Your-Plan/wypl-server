package com.butter.wypl.calendar.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.butter.wypl.auth.annotation.Authenticated;
import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.calendar.data.CalendarType;
import com.butter.wypl.calendar.data.response.BlockListResponse;
import com.butter.wypl.calendar.data.response.CalendarListResponse;
import com.butter.wypl.calendar.data.response.GroupCalendarListResponse;
import com.butter.wypl.calendar.service.CalendarService;
import com.butter.wypl.global.common.Message;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar/v1/calendars")
public class CalendarController {

	private final CalendarService calendarService;

	@GetMapping("/{type}")
	public ResponseEntity<Message<CalendarListResponse>> getPersonalCalendar(
		@Authenticated AuthMember authMember,
		@PathVariable("type") CalendarType type,
		@RequestParam(value = "labelId", required = false) Integer labelId,
		@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
	) {
		return ResponseEntity.ok(
			Message.withBody("개인 페이지 달력 조회 성공",
				calendarService.getCalendarSchedules(authMember.getId(), type, labelId, date))
		);
	}

	@GetMapping("/{type}/{groupId}")
	public ResponseEntity<Message<GroupCalendarListResponse>> getGroupCalendar(
		@Authenticated AuthMember authMember,
		@PathVariable("type") CalendarType type,
		@PathVariable("groupId") int groupId,
		@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
	) {
		return ResponseEntity.ok(
			Message.withBody("그룹 페이지 달력 조회 성공",
				calendarService.getGroupCalendarSchedule(authMember.getId(), type, date, groupId))
		);
	}

	@GetMapping("/years")
	public ResponseEntity<Message<BlockListResponse>> getYearCalendar(
		@Authenticated AuthMember authMember,
		@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
	) {
		return ResponseEntity.ok(
			Message.withBody("일년 달력 조회 성공",
				calendarService.getVisualization(authMember.getId(), date))
		);
	}
}
