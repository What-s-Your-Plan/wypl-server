package com.butter.wypl.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final int statusCode;
	private final String errorCode;
	private final String message;
	private final HttpStatus httpStatus;

	public CustomException(ErrorCode errorCode) {
		this.statusCode = errorCode.getStatusCode();
		this.errorCode = errorCode.getErrorCode();
		this.message = errorCode.getMessage();
		this.httpStatus = errorCode.getHttpStatus();
	}
}
