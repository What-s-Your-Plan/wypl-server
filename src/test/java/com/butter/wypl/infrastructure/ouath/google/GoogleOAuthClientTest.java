package com.butter.wypl.infrastructure.ouath.google;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.infrastructure.exception.InfraErrorCode;
import com.butter.wypl.infrastructure.exception.InfraException;

@MockServiceTest
class GoogleOAuthClientTest {
	@InjectMocks
	private GoogleOAuthClient googleOAuthClient;

	@Mock
	private RestTemplate restTemplate;

	@DisplayName("회원 인증에 성공한다.")
	@Test
	void getOAuthMemberSuccessTest() {
		/* Given */
		String dummyCode = "dummy_code";
		GoogleOAuthResponse googleOAuthResponse =
				new GoogleOAuthResponse("at", 1000L, "tt", "it");

		given(restTemplate.postForEntity(anyString(), any(Map.class), any()))
				.willReturn(ResponseEntity.ok(googleOAuthResponse));

		/* When & Then */
		assertThatCode(() -> googleOAuthClient.getOAuthMember(dummyCode))
				.doesNotThrowAnyException();
	}

	@DisplayName("회원 인증에 실패한다.")
	@Test
	void getOAuthMemberFailedTest() {
		/* Given */
		String dummyCode = "dummy_code";
		GoogleOAuthResponse googleOAuthResponse =
				new GoogleOAuthResponse("at", 1000L, "tt", "it");

		given(restTemplate.postForEntity(anyString(), any(Map.class), any()))
				.willReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		/* When & Then */
		assertThatThrownBy(() -> googleOAuthClient.getOAuthMember(dummyCode))
				.isInstanceOf(InfraException.class)
				.hasMessageContaining(InfraErrorCode.INVALID_OAUTH_REQUEST.getMessage());
	}
}