package com.butter.wypl.global.exception;

public class TooFewRequestSizeException extends GlobalException {
	public TooFewRequestSizeException() {
		super(GlobalErrorCode.TOO_MANY_REQUEST_SIZE);
	}
}
