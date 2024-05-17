package com.butter.wypl.infrastructure.weather;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.butter.wypl.global.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <a href="https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2">OPEN WEATHER ID</a>
 */
@Generated
@AllArgsConstructor
@Getter
public enum WeatherType {
	CLEAR(1, new HashSet<>(List.of(800)), "맑음"),
	CLOUDS(2, new HashSet<>(List.of(801, 802, 803, 804)), "구름"),
	RAIN(3, new HashSet<>(List.of(500, 501, 502, 503, 504, 511, 520, 521, 522, 531)), "비"),
	DRIZZLE(4, new HashSet<>(List.of(300, 301, 302, 310, 311, 312, 313, 314, 321)), "이슬비"),
	SNOW(5, new HashSet<>(List.of(600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622)), "눈"),
	MIST(6, new HashSet<>(List.of(701, 711, 721, 731, 741, 751, 761, 762)), "안개"),
	SQUALL(7, new HashSet<>(List.of(771)), "스콜"),
	TORNADO(8, new HashSet<>(List.of(781)), "태풍"),
	THUNDERSTORM(9, new HashSet<>(List.of(200, 201, 202, 210, 211, 212, 221, 230, 231, 232)), "천둥"),
	;

	private final int weatherId;
	private final Set<Integer> ids;
	private final String description;

	public boolean containsIds(final int weatherId) {
		return ids.contains(weatherId);
	}
}
