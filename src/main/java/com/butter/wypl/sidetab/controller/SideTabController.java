package com.butter.wypl.sidetab.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.butter.wypl.auth.annotation.Authenticated;
import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.global.common.Message;
import com.butter.wypl.sidetab.data.request.DDayUpdateRequest;
import com.butter.wypl.sidetab.data.request.GoalUpdateRequest;
import com.butter.wypl.sidetab.data.request.MemoUpdateRequest;
import com.butter.wypl.sidetab.data.response.DDayWidgetResponse;
import com.butter.wypl.sidetab.data.response.GoalWidgetResponse;
import com.butter.wypl.sidetab.data.response.MemoWidgetResponse;
import com.butter.wypl.sidetab.data.response.WeatherWidgetResponse;
import com.butter.wypl.sidetab.service.SideTabLoadService;
import com.butter.wypl.sidetab.service.SideTabModifyService;
import com.butter.wypl.sidetab.service.WeatherWidgetService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/side")
@RestController
public class SideTabController {
	private final SideTabLoadService sideTabLoadService;
	private final SideTabModifyService sideTabModifyService;
	private final WeatherWidgetService weatherWidgetService;

	@GetMapping("/v1/goals/{goal_id}")
	public ResponseEntity<Message<GoalWidgetResponse>> findGoal(
			@Authenticated AuthMember authMember,
			@PathVariable("goal_id") int goalId
	) {
		GoalWidgetResponse response = sideTabLoadService.findGoal(authMember, goalId);
		return ResponseEntity.ok(Message.withBody("목표를 조회했습니다", response));
	}

	@PatchMapping("/v1/goals/{goal_id}")
	public ResponseEntity<Message<GoalWidgetResponse>> updateGoal(
			@Authenticated AuthMember authMember,
			@PathVariable("goal_id") int goalId,
			@RequestBody GoalUpdateRequest request
	) {
		GoalWidgetResponse response
				= sideTabModifyService.updateGoal(authMember, goalId, request);
		return ResponseEntity.ok(Message.withBody("목표를 수정했습니다", response));
	}

	@GetMapping("/v1/d-day/{d_day_id}")
	public ResponseEntity<Message<DDayWidgetResponse>> findDDay(
			@Authenticated AuthMember authMember,
			@PathVariable("d_day_id") int dDayId
	) {
		DDayWidgetResponse response = sideTabLoadService.findDDay(authMember, dDayId);
		return ResponseEntity.ok(Message.withBody("디데이를 조회했습니다", response));
	}

	@PatchMapping("/v1/d-day/{d_day_id}")
	public ResponseEntity<Message<DDayWidgetResponse>> updateDDay(
			@Authenticated AuthMember authMember,
			@PathVariable("d_day_id") int dDayId,
			@RequestBody DDayUpdateRequest request
	) {
		DDayWidgetResponse response
				= sideTabModifyService.updateDDay(authMember, dDayId, request);
		return ResponseEntity.ok(Message.withBody("디데이를 수정했습니다", response));
	}

	@GetMapping("/v1/memo/{memo_id}")
	public ResponseEntity<Message<MemoWidgetResponse>> findMemo(
			@Authenticated AuthMember authMember,
			@PathVariable("memo_id") int memoId
	) {
		MemoWidgetResponse response = sideTabLoadService.findMemo(authMember, memoId);
		return ResponseEntity.ok(Message.withBody("메모를 조회했습니다", response));
	}

	@PatchMapping("/v1/memo/{memo_id}")
	public ResponseEntity<Message<MemoWidgetResponse>> updateMemo(
			@Authenticated AuthMember authMember,
			@PathVariable("memo_id") int memoId,
			@RequestBody MemoUpdateRequest request
	) {
		MemoWidgetResponse response = sideTabModifyService.updateMemo(authMember, memoId, request);
		return ResponseEntity.ok(Message.withBody("메모를 조회했습니다", response));
	}

	@GetMapping("/v1/weathers")
	public ResponseEntity<Message<WeatherWidgetResponse>> findWeather(
			@Authenticated AuthMember authMember,
			@RequestParam(name = "metric", defaultValue = "true") boolean metric,
			@RequestParam(name = "lang", defaultValue = "true") boolean lang
	) {
		WeatherWidgetResponse response = weatherWidgetService.findCurrentWeather(authMember, metric, lang);
		return ResponseEntity.ok(Message.withBody("날씨 위젯 조회하였습니다.", response));
	}
}
