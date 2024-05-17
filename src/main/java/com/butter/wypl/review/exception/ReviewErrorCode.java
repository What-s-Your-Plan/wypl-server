package com.butter.wypl.review.exception;

import org.springframework.http.HttpStatus;

import com.butter.wypl.global.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReviewErrorCode implements ErrorCode {
	NOT_PERMISSION_TO_REVIEW(HttpStatus.BAD_REQUEST, "REVIEW_001", "해당 리뷰에 접근 권한이 없습니다."),
	EMPTY_CONTENTS(HttpStatus.BAD_REQUEST, "REVIEW_002", "리뷰에 컨텐츠가 없습니다."),
	NOT_APPROPRIATE_TITLE(HttpStatus.BAD_REQUEST, "REVIEW_003", "적합한 제목이 아닙니다.");
	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	@Override
	public int getStatusCode() {
		return httpStatus.value();
	}
}

