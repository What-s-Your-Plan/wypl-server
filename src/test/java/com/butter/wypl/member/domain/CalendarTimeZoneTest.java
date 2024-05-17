package com.butter.wypl.member.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalendarTimeZoneTest {

	@DisplayName("달력의 타임존을 조회한다.")
	@Test
	void getTimeZonesTest() {
		/* Given & When */
		List<CalendarTimeZone> timeZones = CalendarTimeZone.getTimeZones();

		/* Then */
		assertThat(timeZones).size().isNotZero();
	}
}