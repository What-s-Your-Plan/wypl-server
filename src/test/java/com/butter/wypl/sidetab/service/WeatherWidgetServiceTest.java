package com.butter.wypl.sidetab.service;

import static com.butter.wypl.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.infrastructure.weather.OpenWeatherClient;
import com.butter.wypl.infrastructure.weather.WeatherRegion;
import com.butter.wypl.infrastructure.weather.data.OpenWeatherCond;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.sidetab.exception.SideTabErrorCode;
import com.butter.wypl.sidetab.exception.SideTabException;
import com.butter.wypl.sidetab.fixture.WeatherFixture;
import com.butter.wypl.sidetab.repository.WeatherWidgetRepository;

@MockServiceTest
class WeatherWidgetServiceTest {
	@InjectMocks
	private SideTabServiceImpl sideTabService;

	@Mock
	private OpenWeatherClient openWeatherClient;

	@Mock
	private WeatherWidgetRepository weatherWidgetRepository;

	@Mock
	private MemberRepository memberRepository;

	private AuthMember authMember;

	@BeforeEach
	void setUp() {
		authMember = AuthMember.from(0);
	}

	@DisplayName("날씨를 조회한다.")
	@ParameterizedTest
	@EnumSource(WeatherFixture.class)
	void findCurrentWeatherTest(WeatherFixture weatherFixture) {
		/* Given */
		given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(KIM_JEONG_UK.toMember()));

		given(weatherWidgetRepository.findById(any(WeatherRegion.class)))
				.willReturn(Optional.of(weatherFixture.toWeatherWidget()));

		/* When & Then */
		assertThatCode(() -> sideTabService.findCurrentWeather(
				authMember,
				weatherFixture.isMetric(),
				weatherFixture.isLangKr())
		).doesNotThrowAnyException();
	}

	@DisplayName("없는 날씨 식별자를 가져오면 예외를 던진다.")
	@Test
	void invalidFindCurrentWeatherTest() {
		/* Given */
		WeatherFixture weatherFixture = WeatherFixture.DEGREE_KR_KOREA;

		given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(KIM_JEONG_UK.toMember()));

		given(openWeatherClient.fetchWeather(any(OpenWeatherCond.class)))
				.willReturn(weatherFixture.toInvalidOpenWeatherResponse());

		/* When & Then */
		assertThatThrownBy(() -> sideTabService.findCurrentWeather(
				authMember,
				weatherFixture.isMetric(),
				weatherFixture.isLangKr())
		)
				.isInstanceOf(SideTabException.class)
				.hasMessageContaining(SideTabErrorCode.INVALID_WEATHER_ID.getMessage());
	}
}