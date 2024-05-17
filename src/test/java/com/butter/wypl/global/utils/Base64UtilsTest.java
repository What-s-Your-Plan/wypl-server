package com.butter.wypl.global.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Base64UtilsTest {

	@DisplayName("Base64 복호화에 성공한다.")
	@Test
	void decodeSuccess() {
		/* Given */
		String stringAsBase64 = "QkFTRTY0";

		/* When */
		String decode = Base64Utils.decode(stringAsBase64);

		/* Then */
		assertThat(decode).isEqualTo("BASE64");
	}
}