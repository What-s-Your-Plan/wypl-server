package com.butter.wypl.member.data.response;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.butter.wypl.global.utils.LocalDateUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberBirthdayUpdateResponse(
		@JsonProperty("birthday")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		LocalDate birthday,

		@JsonProperty("birthday_as_string")
		String birthdayAsString
) {
	public static MemberBirthdayUpdateResponse from(final LocalDate birthday) {
		return new MemberBirthdayUpdateResponse(birthday, LocalDateUtils.toString(birthday));
	}
}
