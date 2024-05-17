package com.butter.wypl.member.data.response;

import com.butter.wypl.member.domain.CalendarTimeZone;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberTimezoneUpdateResponse(
		@JsonProperty("timezone")
		CalendarTimeZone timeZone
) {
}
