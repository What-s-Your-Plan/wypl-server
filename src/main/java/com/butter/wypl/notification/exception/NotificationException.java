package com.butter.wypl.notification.exception;

import com.butter.wypl.global.exception.CustomException;
import com.butter.wypl.global.exception.ErrorCode;

public class NotificationException extends CustomException {

	public NotificationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
