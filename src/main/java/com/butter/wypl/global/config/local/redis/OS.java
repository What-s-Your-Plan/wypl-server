package com.butter.wypl.global.config.local.redis;

import lombok.Getter;

@Getter
public enum OS {
	MAC("Mac"),
	WINDOWS("Windows 10"),
	LINUX("Linux"),
	UBUNTU("Ubuntu"),
	DEBIAN("Debian"),
	;

	private final String value;

	OS(String value) {
		this.value = value;
	}

	public boolean contains(String osName) {
		return osName.contains(value);
	}
}
