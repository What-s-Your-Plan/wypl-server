package com.butter.wypl.infrastructure.weather.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenWeatherResponse(
		@JsonProperty("weather")
		List<WeatherResponse> weather,
		@JsonProperty("main")
		MainResponse main,
		@JsonProperty("sys")
		SysResponse sys,
		@JsonProperty("dt")
		long dateTime
) {

	public static WeatherResponse ofByWeatherResponse(final int id, final String main, final String desc) {
		return new WeatherResponse(id, main, desc);
	}

	public static MainResponse fromByMainResponse(
			final float temp,
			final float maxTemp,
			final float minTemp
	) {
		return new MainResponse(temp, maxTemp, minTemp);
	}

	public static SysResponse fromBySysResponse(
			final long sunrise,
			final long sunset
	) {
		return new SysResponse(sunrise, sunset);
	}

	public int getWeatherId() {
		return weather.get(0).id();
	}

	public String getWeatherName() {
		return weather.get(0).main();
	}

	public String getWeatherDescription() {
		return weather.get(0).description();
	}

	public float getTemperature() {
		return main.temp();
	}

	public float getMaxTemperature() {
		return main.maxTemp();
	}

	public float getMinTemperature() {
		return main.minTemp();
	}

	public long getSunrise() {
		return sys.sunrise();
	}

	public long getSunset() {
		return sys.sunset();
	}

	public record WeatherResponse(
			@JsonProperty("id")
			int id,
			@JsonProperty("main")
			String main,
			@JsonProperty("description")
			String description
	) {
	}

	public record MainResponse(
			@JsonProperty("temp")
			float temp,
			@JsonProperty("temp_max")
			float maxTemp,
			@JsonProperty("temp_min")
			float minTemp
	) {
	}

	public record SysResponse(
			@JsonProperty("sunrise")
			long sunrise,
			@JsonProperty("sunset")
			long sunset
	) {
	}
}
