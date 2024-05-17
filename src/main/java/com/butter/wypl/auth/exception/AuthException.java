package com.butter.wypl.auth.exception;

import com.butter.wypl.global.exception.CustomException;

public class AuthException extends CustomException {
	public AuthException(AuthErrorCode errorCode) {
		super(errorCode);
	}
}
