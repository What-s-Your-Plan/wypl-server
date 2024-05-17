package com.butter.wypl.global.validator;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.butter.wypl.global.exception.TooFewRequestSizeException;
import com.butter.wypl.global.exception.TooManyRequestSizeException;

class RequestValidatorTest {

	@DisplayName("validateRequestSize 테스트")
	@Nested
	class ValidateRequestSizeTest {

		@DisplayName("검증에 성공한다.")
		@ParameterizedTest
		@ValueSource(ints = {0, 50})
		void successTest(int size) {
			assertThatCode(() -> RequestValidator.validateRequestSize(size))
					.doesNotThrowAnyException();
		}

		@DisplayName("너무 많은 요청은 예외를 던진다.")
		@ParameterizedTest
		@ValueSource(ints = {51, Integer.MAX_VALUE})
		void tooManyRequestTest(int size) {
			assertThatThrownBy(() -> RequestValidator.validateRequestSize(size))
					.isInstanceOf(TooManyRequestSizeException.class);
		}

		@DisplayName("0건 미만의 요청은 예외를 던진다.")
		@ParameterizedTest
		@ValueSource(ints = {-1, Integer.MIN_VALUE})
		void tooFewRequestTest(int size) {
			assertThatThrownBy(() -> RequestValidator.validateRequestSize(size))
					.isInstanceOf(TooFewRequestSizeException.class);
		}
	}
}