package com.butter.wypl.global.exception;

import org.springframework.http.HttpStatus;

public record CustomErrorCode(
	HttpStatus httpStatus,
	String errorCode,
	String message

) implements ErrorCode {
	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public int getStatusCode() {
		return httpStatus.value();
	}
}
