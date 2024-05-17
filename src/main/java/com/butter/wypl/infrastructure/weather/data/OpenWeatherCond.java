package com.butter.wypl.infrastructure.weather.data;

import com.butter.wypl.infrastructure.weather.WeatherRegion;

public record OpenWeatherCond(
		WeatherRegion city,
		boolean isMetric,
		boolean isLangKr
) {
	public static OpenWeatherCond from(
			final WeatherRegion city
	) {
		return new OpenWeatherCond(city, true, true);
	}

	public static OpenWeatherCond of(
			final WeatherRegion city,
			final boolean isMetric,
			final boolean isLangKr
	) {
		return new OpenWeatherCond(city, isMetric, isLangKr);
	}
}
