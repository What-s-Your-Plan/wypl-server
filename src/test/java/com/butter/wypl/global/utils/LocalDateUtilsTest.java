package com.butter.wypl.global.utils;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LocalDateUtilsTest {

	@DisplayName("LocalDate 를 문자열로 변환한다.")
	@Test
	void toStringTest() {
		/* Given */
		LocalDate localDate = LocalDate.of(1998, 11, 24);

		/* When */
		String localDateAsString = LocalDateUtils.toString(localDate);

		/* Then */
		assertThat(localDateAsString).isEqualTo("1998년 11월 24일");
	}
}