package com.butter.wypl.infrastructure.weather.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "open-weather")
public class OpenWeatherProperties {
	private String key;
	private String baseUrl;
}
