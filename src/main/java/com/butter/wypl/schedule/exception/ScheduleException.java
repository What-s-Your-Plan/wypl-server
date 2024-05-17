package com.butter.wypl.schedule.exception;

import com.butter.wypl.global.exception.CustomException;

public class ScheduleException extends CustomException {
	public ScheduleException(ScheduleErrorCode errorCode) {
		super(errorCode);
	}
}
