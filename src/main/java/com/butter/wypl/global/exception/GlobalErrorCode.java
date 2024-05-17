package com.butter.wypl.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GlobalErrorCode implements ErrorCode {
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_001", "알 수 없는 서버의 오류입니다."),
	ALREADY_DELETED_ENTITY(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_002", "복구중 문제가 발생하였습니다."),
	NO_DELETED_ENTITY(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_003", "삭제중 문제가 발생하였습니다."),
	NOT_ALLOWED_EXTENSION(HttpStatus.BAD_REQUEST, "SERVER_004", "업로드한 사진의 확장자가 틀렸습니다."),
	TOO_MANY_REQUEST_SIZE(HttpStatus.BAD_REQUEST, "SERVER_005", "데이터의 요청 건수가 너무 큽니다."),
	TOO_FEW_REQUEST_SIZE(HttpStatus.BAD_REQUEST, "SERVER_006", "데이터의 요청 건수가 0미만 입니다."),

	;

	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	public int getStatusCode() {
		return httpStatus.value();
	}
}
