package com.butter.wypl.calendar.exception;

import org.springframework.http.HttpStatus;

import com.butter.wypl.global.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CalendarErrorCode implements ErrorCode {
	NOT_APPROPRIATE_TYPE(HttpStatus.BAD_REQUEST, "CALENDAR_001", "타입 지정이 잘못되었습니다."),
	;
	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	@Override
	public int getStatusCode() {
		return httpStatus.value();
	}
}

