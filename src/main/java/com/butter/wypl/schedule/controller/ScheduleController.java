package com.butter.wypl.schedule.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.butter.wypl.auth.annotation.Authenticated;
import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.global.common.Message;
import com.butter.wypl.schedule.data.ModificationType;
import com.butter.wypl.schedule.data.request.ScheduleCreateRequest;
import com.butter.wypl.schedule.data.request.ScheduleUpdateRequest;
import com.butter.wypl.schedule.data.response.ScheduleDetailResponse;
import com.butter.wypl.schedule.data.response.ScheduleIdListResponse;
import com.butter.wypl.schedule.data.response.ScheduleResponse;
import com.butter.wypl.schedule.service.ScheduleModifyService;
import com.butter.wypl.schedule.service.ScheduleReadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule/v1/schedules")
public class ScheduleController {

	private final ScheduleModifyService scheduleModifyService;
	private final ScheduleReadService scheduleReadService;

	@GetMapping("/details/{scheduleId}")
	public ResponseEntity<Message<ScheduleDetailResponse>> getDetailScheduleByScheduleId(
		@Authenticated AuthMember authMember,
		@PathVariable int scheduleId
	) {
		return ResponseEntity
			.ok().body(
				Message.withBody("상세 일정 조회 성공",
					scheduleReadService.getDetailScheduleByScheduleId(authMember.getId(), scheduleId))
			);
	}

	@GetMapping("/{scheduleId}")
	public ResponseEntity<Message<ScheduleResponse>> getScheduleByScheduleId(
		@Authenticated AuthMember authMember,
		@PathVariable int scheduleId
	) {
		return ResponseEntity
			.ok().body(
				Message.withBody("간략 일정 조회 성공",
					scheduleReadService.getScheduleByScheduleId(authMember.getId(), scheduleId))
			);
	}

	@PostMapping
	public ResponseEntity<Message<ScheduleDetailResponse>> createSchedule(
		@Authenticated AuthMember authMember,
		@RequestBody ScheduleCreateRequest scheduleCreateRequest
	) {
		return ResponseEntity
			.status(HttpStatus.CREATED).body(
				Message.withBody("일정 생성 성공",
					scheduleModifyService.createSchedule(authMember.getId(), scheduleCreateRequest))
			);
	}

	@PutMapping("/{scheduleId}")
	public ResponseEntity<Message<ScheduleDetailResponse>> updateSchedule(
		@Authenticated AuthMember authMember,
		@PathVariable int scheduleId,
		@RequestBody ScheduleUpdateRequest scheduleUpdateRequest
	) {
		return ResponseEntity
			.ok().body(
				Message.withBody("일정 수정 성공",
					scheduleModifyService.updateSchedule(authMember.getId(), scheduleId, scheduleUpdateRequest))
			);
	}

	@DeleteMapping("/{scheduleId}/{modificationType}")
	public ResponseEntity<Message<ScheduleIdListResponse>> deleteSchedules(
		@Authenticated AuthMember authMember,
		@PathVariable("scheduleId") int scheduleId,
		@PathVariable("modificationType") ModificationType modificationType
	) {
		return ResponseEntity
			.ok().body(
				Message.withBody("일정 삭제 성공",
					scheduleModifyService.deleteSchedule(authMember.getId(), scheduleId, modificationType))
			);
	}

}
