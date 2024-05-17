package com.butter.wypl.auth.exception;

import org.springframework.http.HttpStatus;

import com.butter.wypl.global.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthErrorCode implements ErrorCode {
	NO_SUPPORT_PROVIDER(HttpStatus.BAD_REQUEST, "AUTH_001", "지원하지 않는 소셜로그인 형식입니다."),
	NOT_AUTHORIZATION_MEMBER(HttpStatus.UNAUTHORIZED, "AUTH_002", "로그인이 필요한 기능입니다."),
	INVALID_JWT(HttpStatus.BAD_REQUEST, "AUTH_003", "올바른 인증이 아닙니다."),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_004", "인증 시간이 만료되었습니다."),
	UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "AUTH_005", "지원하지 않는 인증 방식입니다"),
	WRONG_TYPE_TOKEN(HttpStatus.BAD_REQUEST, "AUTH_006", "변조된 인증 입니다."),
	NON_EXISTED_TOKEN(HttpStatus.BAD_REQUEST, "AUTH_007", "로그인 정보가 없습니다.");

	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	public int getStatusCode() {
		return httpStatus.value();
	}
}
