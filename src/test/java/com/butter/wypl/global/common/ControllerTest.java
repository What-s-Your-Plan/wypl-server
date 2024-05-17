package com.butter.wypl.global.common;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.auth.utils.AuthenticatedArgumentResolver;
import com.butter.wypl.auth.utils.JwtProvider;
import com.butter.wypl.file.S3ImageProvider;
import com.butter.wypl.global.annotation.MockControllerTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@MockControllerTest
public abstract class ControllerTest {

	protected static final String AUTHORIZATION_HEADER_VALUE = "Bearer header.payload.signature";

	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected ObjectMapper objectMapper;
	@MockBean
	protected AuthenticatedArgumentResolver authenticatedArgumentResolver;
	@MockBean
	protected JwtProvider jwtProvider;
	@MockBean
	protected S3ImageProvider s3ImageProvider;

	protected String convertToJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new AssertionError("올바르지 않는 JSON 형식입니다.");
		}
	}

	protected void givenMockLoginMember() {
		given(authenticatedArgumentResolver.supportsParameter(any(MethodParameter.class)))
				.willReturn(true);
		given(authenticatedArgumentResolver.resolveArgument(
				any(MethodParameter.class),
				any(ModelAndViewContainer.class),
				any(NativeWebRequest.class),
				any(WebDataBinderFactory.class))
		).willReturn(any(AuthMember.class));
	}
}
