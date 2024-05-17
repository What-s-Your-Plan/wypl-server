package com.butter.wypl.infrastructure.weather;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.butter.wypl.global.annotation.InfraComponent;
import com.butter.wypl.infrastructure.exception.InfraErrorCode;
import com.butter.wypl.infrastructure.exception.InfraException;
import com.butter.wypl.infrastructure.weather.data.OpenWeatherCond;
import com.butter.wypl.infrastructure.weather.data.OpenWeatherResponse;
import com.butter.wypl.infrastructure.weather.properties.OpenWeatherProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@InfraComponent
public class OpenWeatherClient {

	private final RestTemplate restTemplate;
	private final OpenWeatherProperties openWeatherProperties;

	public OpenWeatherResponse fetchWeather(OpenWeatherCond cond) {
		String url = getUrl(cond);
		ResponseEntity<OpenWeatherResponse> response = restTemplate.getForEntity(
				url,
				OpenWeatherResponse.class
		);

		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		}
		if (response.getStatusCode().is5xxServerError()) {
			throw new InfraException(InfraErrorCode.OPEN_WEATHER_INTERNAL_SERVER_ERROR);
		}
		throw new InfraException(InfraErrorCode.INVALID_OPEN_WEATHER_REQUEST);
	}

	private String getUrl(OpenWeatherCond cond) {
		StringBuilder url = new StringBuilder(openWeatherProperties.getBaseUrl())
				.append("?appid=")
				.append(openWeatherProperties.getKey());

		addParamByCity(url, cond.city());
		addParamByLang(url, cond.isLangKr());
		addParamByUnits(url, cond.isMetric());

		return url.toString();
	}

	private void addParamByCity(StringBuilder url, WeatherRegion region) {
		url.append("&q=").append(region.getCityEn());
	}

	private void addParamByLang(StringBuilder url, boolean isLangKr) {
		if (isLangKr) {
			url.append("&lang=kr");
		}
	}

	private void addParamByUnits(StringBuilder url, boolean isMetric) {
		if (isMetric) {
			url.append("&units=metric");
		}
	}
}
