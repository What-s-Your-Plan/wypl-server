package com.butter.wypl.auth.annotation;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureMockMvc
@SpringBootTest
class AuthenticatedArgumentResolverTest {

	private static final String INVALID_AUTHORIZATION_HEADER_VALUE = "header.payload.signature";

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("사용자의 인증 토큰이 올바르지 않으면 예외를 던진다.")
	@Test
	void invalidAuthorizationTest() throws Exception {
		/* Given */
		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/auth/v1/logout")
						.header(HttpHeaders.AUTHORIZATION, INVALID_AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
		);

		/* Then */
		actions.andDo(print())
				.andExpect(status().is4xxClientError());
	}
}