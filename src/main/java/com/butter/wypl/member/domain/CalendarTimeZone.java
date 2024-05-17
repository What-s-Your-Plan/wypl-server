package com.butter.wypl.member.domain;

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CalendarTimeZone {
	KOREA(TimeZone.getTimeZone("Asia/Seoul")),
	WEST_USA(TimeZone.getTimeZone("America/Los_Angeles")),
	EAST_USA(TimeZone.getTimeZone("America/New_York")),
	ENGLAND(TimeZone.getTimeZone("Europe/London"));

	private final TimeZone timeZone;

	public static List<CalendarTimeZone> getTimeZones() {
		return Arrays.stream(CalendarTimeZone.values()).toList();
	}
}
