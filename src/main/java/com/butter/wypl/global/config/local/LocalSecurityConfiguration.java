package com.butter.wypl.global.config.local;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Profile({"local", "default", "test"})
@RequiredArgsConstructor
@Configuration
public class LocalSecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(httpSecurityCsrfConfigurer ->
						httpSecurityCsrfConfigurer.ignoringRequestMatchers(PathRequest.toH2Console())
								.disable())
				.cors(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.headers(httpSecurityHeadersConfigurer ->
						httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
				.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
						authorizationManagerRequestMatcherRegistry
								.requestMatchers(PathRequest.toH2Console()).permitAll()
								.anyRequest().permitAll());
		return http.build();
	}
}
