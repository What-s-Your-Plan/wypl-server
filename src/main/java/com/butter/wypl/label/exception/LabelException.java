package com.butter.wypl.label.exception;

import com.butter.wypl.global.exception.CustomException;

public class LabelException extends CustomException {
	public LabelException(LabelErrorCode errorCode) {
		super(errorCode);
	}
}
