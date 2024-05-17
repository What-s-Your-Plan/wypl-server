package com.butter.wypl.review.exception;

import com.butter.wypl.global.exception.CustomException;

public class ReviewException extends CustomException {
	public ReviewException(ReviewErrorCode errorCode) {
		super(errorCode);
	}
}
