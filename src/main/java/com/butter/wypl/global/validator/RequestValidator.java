package com.butter.wypl.global.validator;

import com.butter.wypl.global.annotation.Generated;
import com.butter.wypl.global.exception.CallConstructorException;
import com.butter.wypl.global.exception.TooFewRequestSizeException;
import com.butter.wypl.global.exception.TooManyRequestSizeException;

public class RequestValidator {
	@Generated
	private RequestValidator() {
		throw new CallConstructorException();
	}

	public static void validateRequestSize(int size) {
		if (size > 50) {
			throw new TooManyRequestSizeException();
		}
		if (size < 0) {
			throw new TooFewRequestSizeException();
		}
	}
}
