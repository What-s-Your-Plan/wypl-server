package com.butter.wypl.global.config.local.redis;

import java.io.IOException;

public class RedisAvailablePortFindForLinux extends RedisAvailablePortFind {

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
