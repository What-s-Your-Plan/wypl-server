package com.butter.wypl.global.utils;

import java.time.LocalDate;

import com.butter.wypl.global.annotation.Generated;
import com.butter.wypl.global.exception.CallConstructorException;

public class LocalDateUtils {

	@Generated
	private LocalDateUtils() {
		throw new CallConstructorException();
	}

	public static String toString(final LocalDate date) {
		return String.format("%d년 %d월 %d일", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
	}
}
