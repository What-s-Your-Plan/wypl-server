package com.butter.wypl.infrastructure.exception;

import org.springframework.http.HttpStatus;

import com.butter.wypl.global.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InfraErrorCode implements ErrorCode {
	BODY_IS_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "INFRA_001", "데이터가 존재하지 않습니다."),
	INVALID_OAUTH_REQUEST(HttpStatus.BAD_REQUEST, "INFRA_002", "올바르지 않은 인증 요청입니다."),
	INVALID_OPEN_WEATHER_REQUEST(HttpStatus.BAD_REQUEST, "INFRA_003", "올바르지 않은 요청입니다."),
	OPEN_WEATHER_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INFRA_004", "외부 서비스의 오류입니다.");

	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	public int getStatusCode() {
		return httpStatus.value();
	}
}
