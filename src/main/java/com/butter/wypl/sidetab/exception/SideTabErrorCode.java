package com.butter.wypl.sidetab.exception;

import org.springframework.http.HttpStatus;

import com.butter.wypl.global.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SideTabErrorCode implements ErrorCode {
	NOT_EXIST_SIDE(HttpStatus.BAD_REQUEST, "SIDE_001", "존재하지 않는 내용입니다."),
	INVALID_WEATHER_ID(HttpStatus.BAD_REQUEST, "SIDE_002", "존재하지 않는 날씨입니다.");
	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	public int getStatusCode() {
		return httpStatus.value();
	}
}
