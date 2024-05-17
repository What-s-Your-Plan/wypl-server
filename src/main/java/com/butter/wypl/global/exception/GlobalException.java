package com.butter.wypl.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
	private final HttpStatus httpStatus;
	private final int statusCode;
	private final String message;

	public GlobalException(
			ErrorCode errorCode
	) {
		this.httpStatus = errorCode.getHttpStatus();
		this.statusCode = errorCode.getStatusCode();
		this.message = errorCode.getMessage();
	}
}
