package com.butter.wypl.global.exception;

public class TooManyRequestSizeException extends GlobalException {
	public TooManyRequestSizeException() {
		super(GlobalErrorCode.TOO_FEW_REQUEST_SIZE);
	}
}