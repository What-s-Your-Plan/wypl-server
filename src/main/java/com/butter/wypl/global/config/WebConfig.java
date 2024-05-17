package com.butter.wypl.global.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.butter.wypl.auth.utils.AuthenticatedArgumentResolver;
import com.butter.wypl.auth.utils.JwtProvider;
import com.butter.wypl.global.common.interceptor.LoggingInterceptor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final LoggingInterceptor loggingInterceptor;
	private final JwtProvider jwtProvider;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new AuthenticatedArgumentResolver(jwtProvider));
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "FETCH", "OPTIONS")
				.allowedHeaders("*")
				.allowedOrigins("*")
				.maxAge(5000);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loggingInterceptor);
	}
}