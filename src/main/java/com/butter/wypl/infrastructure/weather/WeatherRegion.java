package com.butter.wypl.infrastructure.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WeatherRegion {
	KOREA("Asia/Seoul", "Seoul", "서울"),
	WEST_USA("America/Los_Angeles", "Los Angeles", "로스엔젤레스"),
	EAST_USA("America/New_York", "New York", "뉴욕"),
	ENGLAND("Europe/London", "London", "런던");

	private final String timeZone;
	private final String cityEn;
	private final String cityKr;
}
