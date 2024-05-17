package com.butter.wypl.infrastructure.weather;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.infrastructure.exception.InfraErrorCode;
import com.butter.wypl.infrastructure.exception.InfraException;
import com.butter.wypl.infrastructure.weather.data.OpenWeatherResponse;
import com.butter.wypl.infrastructure.weather.properties.OpenWeatherProperties;
import com.butter.wypl.sidetab.fixture.WeatherFixture;

@MockServiceTest
class OpenWeatherClientTest {

	@InjectMocks
	private OpenWeatherClient openWeatherClient;

	@Mock
	private OpenWeatherProperties openWeatherProperties;

	@Mock
	private RestTemplate restTemplate;

	@BeforeEach
	void setUp() {
		given(openWeatherProperties.getBaseUrl()).willReturn("url");
		given(openWeatherProperties.getKey()).willReturn("key");
	}

	@DisplayName("API 200번 호출 테스트")
	@ParameterizedTest
	@EnumSource(WeatherFixture.class)
	void fetchWeather20xTest(WeatherFixture weatherFixture) {
		/* Given */
		given(restTemplate.getForEntity(anyString(), eq(OpenWeatherResponse.class)))
				.willReturn(ResponseEntity.ok(weatherFixture.toOpenWeatherResponse()));

		/* When & Then */
		assertThatCode(() -> openWeatherClient.fetchWeather(weatherFixture.toCond()))
				.doesNotThrowAnyException();
	}

	@DisplayName("API 400번 호출 테스트")
	@ParameterizedTest
	@EnumSource(WeatherFixture.class)
	void fetchWeather40xTest(WeatherFixture weatherFixture) {
		/* Given */
		given(restTemplate.getForEntity(anyString(), eq(OpenWeatherResponse.class)))
				.willReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		/* When & Then */
		assertThatThrownBy(() -> openWeatherClient.fetchWeather(weatherFixture.toCond()))
				.isInstanceOf(InfraException.class)
				.hasMessageContaining(InfraErrorCode.INVALID_OPEN_WEATHER_REQUEST.getMessage());
	}

	@DisplayName("API 500번 호출 테스트")
	@ParameterizedTest
	@EnumSource(WeatherFixture.class)
	void fetchWeather50xTest(WeatherFixture weatherFixture) {
		/* Given */
		given(restTemplate.getForEntity(anyString(), eq(OpenWeatherResponse.class)))
				.willReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

		/* When & Then */
		assertThatThrownBy(() -> openWeatherClient.fetchWeather(weatherFixture.toCond()))
				.isInstanceOf(InfraException.class)
				.hasMessageContaining(InfraErrorCode.OPEN_WEATHER_INTERNAL_SERVER_ERROR.getMessage());
	}
}