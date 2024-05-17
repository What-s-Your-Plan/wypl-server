package com.butter.wypl.member.data.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberBirthdayUpdateRequest(
		@JsonProperty("birthday")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		LocalDate birthday
) {
}
