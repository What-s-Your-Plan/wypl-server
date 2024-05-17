package com.butter.wypl.global.config.local.redis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisAvailablePortFindForWindows extends RedisAvailablePortFind {

	@Override
	public int findAvailablePort(int redisPort) {
		for (int port = 10000; port <= 65535; port++) {
			if (!isRunning(port)) {
				return port;
			}
		}
		throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
	}

	@Override
	public boolean isRedisRunning(int redisPort) {
		return isRunning(redisPort);
	}

	private boolean isRunning(final int port) {
		try {
			ProcessBuilder builder = new ProcessBuilder("netstat", "-nat");
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("LISTEN") && line.contains(String.valueOf(port))) {
					return true;
				}
			}
		} catch (IOException e) {
			log.error("Windows Command Error", e);
		}
		return false;
	}
}
