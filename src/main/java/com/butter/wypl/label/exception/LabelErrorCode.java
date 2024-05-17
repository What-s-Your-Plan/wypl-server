package com.butter.wypl.label.exception;

import org.springframework.http.HttpStatus;

import com.butter.wypl.global.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LabelErrorCode implements ErrorCode {
	NO_PERMISSION_UPDATE(HttpStatus.BAD_REQUEST, "LABEL_001", "해당 라벨을 바꿀 권한이 없습니다."),
	NOT_FOUND(HttpStatus.BAD_REQUEST, "LABEL_002", "해당 라벨이 존재하지 않습니다."),
	NOT_APPROPRIATE_COLOR_CODE(HttpStatus.BAD_REQUEST, "LABEL_003", "색상 코드가 적절하지 않습니다."),
	NOT_APPROPRIATE_TITLE(HttpStatus.BAD_REQUEST, "LABEL_004", "제목이 적절하지 않습니다."),
	;
	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	@Override
	public int getStatusCode() {
		return httpStatus.value();
	}
}

