package com.butter.wypl.todo.exception;

import com.butter.wypl.global.exception.CustomException;
import com.butter.wypl.global.exception.ErrorCode;

public class TodoException extends CustomException {

	public TodoException(ErrorCode errorCode) {
		super(errorCode);
	}
}
