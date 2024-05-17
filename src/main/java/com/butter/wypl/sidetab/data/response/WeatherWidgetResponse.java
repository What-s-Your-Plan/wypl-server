package com.butter.wypl.sidetab.data.response;

import com.butter.wypl.sidetab.domain.cache.WeatherWidget;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record WeatherWidgetResponse(
		@JsonProperty("city")
		String city,
		@JsonProperty("weather_id")
		int weatherId,
		@JsonProperty("temp")
		int temp,
		@JsonProperty("min_temp")
		int minTemp,
		@JsonProperty("max_temp")
		int maxTemp,
		@JsonProperty("update_time")
		String updateTime,
		@JsonProperty("main")
		String main,
		@JsonProperty("desc")
		String desc,
		@JsonProperty("is_sunrise")
		boolean isSunrise
) {
	public static WeatherWidgetResponse of(
			final WeatherWidget weatherWidget,
			final boolean isLangKr,
			final boolean isSunrise
	) {
		return WeatherWidgetResponse.builder()
				.city(isLangKr ? weatherWidget.getWeatherRegion().getCityKr()
						: weatherWidget.getWeatherRegion().getCityEn())
				.weatherId(weatherWidget.getWeatherId())
				.temp(weatherWidget.getTemp())
				.minTemp(weatherWidget.getMinTemp())
				.maxTemp(weatherWidget.getMaxTemp())
				.updateTime(weatherWidget.getUpdateTime())
				.main(weatherWidget.getMain())
				.desc(weatherWidget.getDesc())
				.isSunrise(isSunrise)
				.build();
	}
}
