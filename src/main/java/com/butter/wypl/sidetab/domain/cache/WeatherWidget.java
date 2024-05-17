package com.butter.wypl.sidetab.domain.cache;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import com.butter.wypl.infrastructure.weather.WeatherRegion;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(timeToLive = 60 * 60)
@Getter
public class WeatherWidget {
	@Id
	private WeatherRegion weatherRegion;
	private int weatherId;
	private int temp;
	private int minTemp;
	private int maxTemp;
	private String updateTime;
	private String main;
	private String desc;
	private long sunrise;
	private long sunset;
}
