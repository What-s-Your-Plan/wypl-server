package com.butter.wypl.global.exception;

import lombok.Getter;

@Getter
public class CallConstructorException extends RuntimeException {
	private final String message;

	public CallConstructorException() {
		this.message = "해당 객체는 생성자를 호출할 수 없습니다.";
	}
}