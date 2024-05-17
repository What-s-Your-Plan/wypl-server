package com.butter.wypl.auth.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import com.butter.wypl.auth.data.response.AuthTokensResponse;
import com.butter.wypl.auth.service.AuthService;
import com.butter.wypl.global.common.ControllerTest;

class AuthControllerTest extends ControllerTest {

	@Autowired
	private AuthController authController;

	@MockBean
	private AuthService authService;

	@DisplayName("회원 가입 및 로그인한다.")
	@Test
	void signInTest() throws Exception {
		/* Given */
		AuthTokensResponse response = new AuthTokensResponse(0, "at", "rt");
		given(authService.generateTokens(any(String.class), any(String.class)))
				.willReturn(response);

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.post(
								"/auth/v1/sign-in/{provider}?code={code}",
								"google",
								"dummy_code"
						)
						.contentType(MediaType.APPLICATION_JSON)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("auth/sign-in",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("provider").description("소셜 로그인 제공자")
						),
						queryParameters(
								parameterWithName("code").description("인증 코드")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.member_id").type(JsonFieldType.NUMBER)
										.description("회원 식별자"),
								fieldWithPath("body.access_token").type(JsonFieldType.STRING)
										.description("JWT Access Token"),
								fieldWithPath("body.refresh_token").type(JsonFieldType.STRING)
										.description("JWT Refresh Token")
						)
				))
				.andExpect(status().isCreated());
	}

	@DisplayName("JWT 재발급 한다.")
	@Test
	void reissueTest() throws Exception {
		/* Given */
		AuthTokensResponse response = new AuthTokensResponse(0, "at", "rt");
		given(authService.reissueTokens(any(String.class)))
				.willReturn(response);

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.put(
								"/auth/v1/reissue?refresh_token={refresh_token}",
								"token"
						)
						.contentType(MediaType.APPLICATION_JSON)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("auth/reissue",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						queryParameters(
								parameterWithName("refresh_token").description("Refresh Token")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.member_id").type(JsonFieldType.NUMBER)
										.description("회원 식별자"),
								fieldWithPath("body.access_token").type(JsonFieldType.STRING)
										.description("JWT Access Token"),
								fieldWithPath("body.refresh_token").type(JsonFieldType.STRING)
										.description("JWT Refresh Token")
						)
				))
				.andExpect(status().isCreated());
	}

	@DisplayName("사용자가 로그아웃한다.")
	@Test
	void logoutTest() throws Exception {
		/* Given */
		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/auth/v1/logout")
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("auth/logout",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
						)
				))
				.andExpect(status().isOk());
	}
}