package com.butter.wypl.member.data.request;

import com.butter.wypl.member.domain.CalendarTimeZone;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberTimezoneUpdateRequest(
		@JsonProperty("timezone")
		CalendarTimeZone timeZone
) {
}
