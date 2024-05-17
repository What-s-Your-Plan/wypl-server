package com.butter.wypl.schedule.exception;

import org.springframework.http.HttpStatus;

import com.butter.wypl.global.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ScheduleErrorCode implements ErrorCode {
	NOT_APPROPRIATE_REPETITION_CYCLE(HttpStatus.BAD_REQUEST, "SCHEDULE_001", "반복 주기 입력이 잘못되었습니다."),
	NO_SCHEDULE(HttpStatus.BAD_REQUEST, "SCHEDULE_002", "해당 스케줄이 없습니다."),
	NOT_PERMISSION_TO_SCHEDUEL(HttpStatus.BAD_REQUEST, "SCHEDULE_003", "스케줄에 접근 권한이 없습니다."),
	NOT_APPROPRIATE_MODIFICATION_TYPE(HttpStatus.BAD_REQUEST, "SCHEDULE_004", "삭제 유형 입력이 잘못되었습니다."),
	NO_REPETITION(HttpStatus.BAD_REQUEST, "SCHEDULE_005", "해당 반복이 존재하지 않습니다."),
	NOT_APPROPRIATE_TITLE(HttpStatus.BAD_REQUEST, "SCHEDULE_006", "제목이 적절하지 않습니다."),
	NOT_APPROPRIATE_DURATION(HttpStatus.BAD_REQUEST, "SCHEDULE_007", "일정 기간이 적절하지 않습니다."),
	NOT_APPROPRIATE_DAY_OF_WEEK(HttpStatus.BAD_REQUEST, "SCHEDULE_008", "일정 반복의 dayOfWeek 값이 잘못되었습니다."),
	NOT_APPROPRIATE_REPETITION_DURATION(HttpStatus.BAD_REQUEST, "SCHEDULE_009", "일정 반복의 기간이 적절하지 않습니다."),
	NOT_EXIST_REPETITION_START_DATE(HttpStatus.BAD_REQUEST, "SCHEDULE_010", "일정 반복의 시작 시간이 없습니다."),
	NOT_APPROPRIATE_DATE(HttpStatus.BAD_REQUEST, "SCHEDULE_011", "일정 설정 시간이 적절하지 않습니다."),
	NOT_APPROPRIATE_WEEK(HttpStatus.BAD_REQUEST, "SCHEDULE_012", "일정 주 범위가 적절하지 않습니다.");
	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	@Override
	public int getStatusCode() {
		return httpStatus.value();
	}
}

