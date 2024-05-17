package com.butter.wypl.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	private String accessKey;
	private String refreshKey;
}
