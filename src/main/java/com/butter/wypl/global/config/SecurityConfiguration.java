package com.butter.wypl.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Profile({"dev", "blue", "green"})
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.headers(httpSecurityHeadersConfigurer ->
				httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
			.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
				authorizationManagerRequestMatcherRegistry.anyRequest().permitAll());
		return http.build();
	}
}
