package com.butter.wypl.global.config.local.redis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisAvailablePortFindForMac extends RedisAvailablePortFind {

	@Override
	public int findAvailablePort(final int redisPort) throws IOException {
		for (int port = 10000; port <= 65535; port++) {
			Process process = executeGrepProcessCommand(port);
			if (!isRunning(process)) {
				return port;
			}
		}
		throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
	}

	@Override
	public boolean isRedisRunning(final int redisPort) throws IOException {
		return isRunning(executeGrepProcessCommand(redisPort));
	}

	private boolean isRunning(Process process) {
		String line;
		StringBuilder pidInfo = new StringBuilder();
		try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			while ((line = input.readLine()) != null) {
				pidInfo.append(line);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return StringUtils.hasText(pidInfo.toString());
	}

	private Process executeGrepProcessCommand(
			final int port
	) throws IOException {
		String command = String.format("netstat -nat | grep LISTEN | grep %d", port);
		String[] shell = new String[] {"/bin/sh", "-c", command};
		return Runtime.getRuntime().exec(shell);
	}
}
