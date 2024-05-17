package com.butter.wypl.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
	HttpStatus getHttpStatus();

	String getErrorCode();

	String getMessage();

	int getStatusCode();
}
