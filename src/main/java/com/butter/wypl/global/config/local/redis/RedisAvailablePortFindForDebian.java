package com.butter.wypl.global.config.local.redis;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisAvailablePortFindForDebian extends RedisAvailablePortFind {

	private final RedisAvailablePortFindForMac redisAvailablePortFindForMac = new RedisAvailablePortFindForMac();

	@Override
	public int findAvailablePort(int redisPort) throws IOException {
		return redisAvailablePortFindForMac.findAvailablePort(redisPort);
	}

	@Override
	public boolean isRedisRunning(int redisPort) throws IOException {
		return redisAvailablePortFindForMac.isRedisRunning(redisPort);
	}
}
