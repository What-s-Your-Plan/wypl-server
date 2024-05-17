package com.butter.wypl.infrastructure.ouath.google;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.global.exception.CustomException;
import com.butter.wypl.global.exception.GlobalErrorCode;
import com.butter.wypl.infrastructure.exception.InfraErrorCode;
import com.butter.wypl.infrastructure.exception.InfraException;
import com.butter.wypl.infrastructure.ouath.OAuthResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@MockServiceTest
class GoogleOAuthProviderTest {
	@InjectMocks
	private GoogleOAuthProvider googleOAuthProvider;

	@Mock
	private GoogleOAuthClient googleOAuthClient;
	@Mock
	private ObjectMapper objectMapper;

	@DisplayName("구글의 인증된 사용자를 정보를 추출한다.")
	@Test
	void getOAuthMemberSuccessTest() {
		/* Given */
		String code = "dummy_code";

		GoogleOAuthResponse googleOAuthResponse = new GoogleOAuthResponse(
				"header.payload.at_signature",
				1000L,
				"tt",
				"header.payload.id_signature"
		);

		given(googleOAuthClient.getOAuthMember(code))
				.willReturn(googleOAuthResponse);

		/* When & Then */
		assertThatCode(() -> googleOAuthProvider.getOAuthMember(code))
				.doesNotThrowAnyException();
	}

	@DisplayName("올바르지 않은 OAuthMember 형식이면 예외를 던진다.")
	@Test
	void getOAuthMemberFailedTest() throws JsonProcessingException {
		/* Given */
		String code = "dummy_code";

		GoogleOAuthResponse googleOAuthResponse = new GoogleOAuthResponse(
				"header.payload.at_signature",
				1000L,
				"tt",
				"header.payload.id_signature"
		);

		given(googleOAuthClient.getOAuthMember(code))
				.willReturn(googleOAuthResponse);
		given(objectMapper.readValue(anyString(), eq(GoogleOAuthMember.class)))
				.willThrow(JsonProcessingException.class);

		/* When & Then */
		assertThatThrownBy(() -> googleOAuthProvider.getOAuthMember(code))
				.isInstanceOf(CustomException.class)
				.hasMessageContaining(GlobalErrorCode.INTERNAL_SERVER_ERROR.getMessage());
	}

	@DisplayName("다른 소셜로그인 제공자면 예외를 던진다.")
	@Test
	void invalidProviderTest() {
		/* Given */
		String code = "dummy_code";

		given(googleOAuthClient.getOAuthMember(code))
				.willReturn(any(OAuthResponse.class));

		/* When & Then */
		assertThatThrownBy(() -> googleOAuthProvider.getOAuthMember(code))
				.isInstanceOf(InfraException.class)
				.hasMessageContaining(InfraErrorCode.INVALID_OAUTH_REQUEST.getMessage());
	}
}