package com.butter.wypl.calendar.data.response;

import java.time.LocalDate;

public record BlockResponse(
	LocalDate date,
	long time
) {
}
