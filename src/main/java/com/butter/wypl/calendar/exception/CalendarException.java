package com.butter.wypl.calendar.exception;

import com.butter.wypl.global.exception.CustomException;

public class CalendarException extends CustomException {
	public CalendarException(CalendarErrorCode errorCode) {
		super(errorCode);
	}
}
