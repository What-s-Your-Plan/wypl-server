package com.butter.wypl.file.exception;

import org.springframework.http.HttpStatus;

import com.butter.wypl.global.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileErrorCode implements ErrorCode {
	HAVE_NOT_FILENAME(HttpStatus.BAD_REQUEST, "FILE_001", "파일의 이름이 존재하지 않습니다."),
	CONVERT_FILE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_002", "내부 서버문제로 업로드에 실패하였습니다."),
	INVALID_FILE(HttpStatus.BAD_REQUEST, "FILE_003", "잘못된 파일입니다.")
	;

	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	public int getStatusCode() {
		return httpStatus.value();
	}
}
