package com.butter.wypl.infrastructure.exception;

import com.butter.wypl.global.exception.CustomException;

public class InfraException extends CustomException {
	public InfraException(InfraErrorCode errorCode) {
		super(errorCode);
	}
}
