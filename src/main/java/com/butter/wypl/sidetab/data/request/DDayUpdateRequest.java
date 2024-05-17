package com.butter.wypl.sidetab.data.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DDayUpdateRequest(
		@JsonProperty("title")
		String title,
		@JsonProperty("date")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		LocalDate date
) {
}
