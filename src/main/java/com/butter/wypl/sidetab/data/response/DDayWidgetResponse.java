package com.butter.wypl.sidetab.data.response;

import java.time.LocalDate;

import com.butter.wypl.global.utils.LocalDateUtils;
import com.butter.wypl.sidetab.domain.embedded.DDayWidget;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DDayWidgetResponse(
		@JsonProperty("title")
		String title,
		@JsonProperty("d_day")
		String dDay,
		@JsonProperty("date")
		String date,
		@JsonProperty("local_date")
		LocalDate localDate
) {
	public static DDayWidgetResponse of(
			final DDayWidget dDay,
			final String dayAsString) {
		return new DDayWidgetResponse(
				dDay.getTitle(),
				dayAsString,
				LocalDateUtils.toString(dDay.getValue()),
				dDay.getValue());
	}
}
