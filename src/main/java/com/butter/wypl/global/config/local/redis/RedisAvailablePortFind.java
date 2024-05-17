package com.butter.wypl.global.config.local.redis;

import java.io.IOException;

public abstract class RedisAvailablePortFind {
	public abstract int findAvailablePort(final int redisPort) throws IOException;

	public abstract boolean isRedisRunning(final int redisPort) throws IOException;
}
