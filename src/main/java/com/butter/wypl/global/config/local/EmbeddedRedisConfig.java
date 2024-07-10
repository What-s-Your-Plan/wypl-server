package com.butter.wypl.global.config.local;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.butter.wypl.global.config.local.redis.OS;
import com.butter.wypl.global.config.local.redis.RedisAvailablePortFind;
import com.butter.wypl.global.config.local.redis.RedisAvailablePortFindForDebian;
import com.butter.wypl.global.config.local.redis.RedisAvailablePortFindForLinux;
import com.butter.wypl.global.config.local.redis.RedisAvailablePortFindForMac;
import com.butter.wypl.global.config.local.redis.RedisAvailablePortFindForUbuntu;
import com.butter.wypl.global.config.local.redis.RedisAvailablePortFindForWindows;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

@Slf4j
@Profile({"local", "test"})
@Configuration
public class EmbeddedRedisConfig {

	private static final String OS_NAME = System.getProperty("os.name");

	@Value("${spring.data.redis.port}")
	private int redisPort;

	private RedisServer redisServer;

	@PostConstruct
	private void start() throws IOException {
		RedisAvailablePortFind findAvailablePortUtils = getRedisAvailablePortFind();

		int port = findAvailablePortUtils.isRedisRunning(redisPort)
				? findAvailablePortUtils.findAvailablePort(redisPort)
				: redisPort;
		log.info("Embedded Redis Running Port : [{}]", port);

		redisServer = new RedisServer(port);
		redisServer.start();
	}

	@PreDestroy
	private void stop() throws IOException {
		if (redisServer != null) {
			redisServer.stop();
		}
	}

	private RedisAvailablePortFind getRedisAvailablePortFind() {
		if (OS.MAC.contains(OS_NAME)) {
			return new RedisAvailablePortFindForMac();
		} else if (OS.WINDOWS.contains(OS_NAME)) {
			return new RedisAvailablePortFindForWindows();
		} else if (OS.UBUNTU.contains(OS_NAME)) {
			return new RedisAvailablePortFindForUbuntu();
		} else if (OS.LINUX.contains(OS_NAME)) {
			return new RedisAvailablePortFindForLinux();
		} else if (OS.DEBIAN.contains(OS_NAME)) {
			return new RedisAvailablePortFindForDebian();
		}
		throw new IllegalArgumentException("Unsupported OS : " + OS_NAME);
	}
}
