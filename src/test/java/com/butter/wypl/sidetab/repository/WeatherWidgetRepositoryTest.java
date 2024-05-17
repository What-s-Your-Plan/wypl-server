package com.butter.wypl.sidetab.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.RedisRepositoryTest;
import com.butter.wypl.sidetab.domain.cache.WeatherWidget;
import com.butter.wypl.sidetab.fixture.WeatherFixture;

import jakarta.persistence.EntityManager;

@RedisRepositoryTest
class WeatherWidgetRepositoryTest {

	@Autowired
	private WeatherWidgetRepository weatherWidgetRepository;

	@Autowired
	private EntityManager entityManager;

	@DisplayName("Weather 정보를 저장한다.")
	@Test
	void saveTet() {
		/* Given */
		WeatherWidget weatherWidget = WeatherFixture.DEGREE_KR_KOREA.toWeatherWidget();

		/* When */
		WeatherWidget savedWeatherWidget = weatherWidgetRepository.save(weatherWidget);

		/* Then */
		assertThat(savedWeatherWidget).isNotNull();
	}

	@DisplayName("Weather 정보를 조회한다.")
	@Test
	void findTest() {
		/* Given */
		WeatherWidget weatherWidget = WeatherFixture.DEGREE_KR_KOREA.toWeatherWidget();
		WeatherWidget savedWeatherWidget = weatherWidgetRepository.save(weatherWidget);

		entityManager.flush();
		entityManager.clear();

		/* When */
		Optional<WeatherWidget> findWeatherWidget =
				weatherWidgetRepository.findById(savedWeatherWidget.getWeatherRegion());

		/* Then */
		assertThat(findWeatherWidget).isPresent();

	}
}