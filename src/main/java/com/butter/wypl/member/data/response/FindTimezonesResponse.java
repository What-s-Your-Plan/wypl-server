package com.butter.wypl.member.data.response;

import java.util.List;

import com.butter.wypl.member.domain.CalendarTimeZone;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FindTimezonesResponse(
		@JsonProperty("member_timezone")
		CalendarTimeZone memberTimeZone,
		@JsonProperty("timezones")
		List<CalendarTimeZone> timezones,
		@JsonProperty("timezone_count")
		int timezoneCount
) {
	public static FindTimezonesResponse of(
			final CalendarTimeZone timeZone,
			final List<CalendarTimeZone> timezones
	) {
		return new FindTimezonesResponse(timeZone, timezones, timezones.size());
	}
}
