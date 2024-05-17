package com.butter.wypl.infrastructure.ouath.google;

import org.springframework.stereotype.Component;

import com.butter.wypl.global.exception.CustomException;
import com.butter.wypl.global.exception.GlobalErrorCode;
import com.butter.wypl.global.utils.Base64Utils;
import com.butter.wypl.infrastructure.exception.InfraErrorCode;
import com.butter.wypl.infrastructure.exception.InfraException;
import com.butter.wypl.infrastructure.ouath.OAuthMember;
import com.butter.wypl.infrastructure.ouath.OAuthProvider;
import com.butter.wypl.infrastructure.ouath.OAuthResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class GoogleOAuthProvider implements OAuthProvider {

	private final GoogleOAuthClient googleOAuthClient;
	private final ObjectMapper objectMapper;

	@Override
	public OAuthMember getOAuthMember(
			final String code
	) {
		return getGoogleOAuthMember(googleOAuthClient.getOAuthMember(code));
	}

	private GoogleOAuthMember getGoogleOAuthMember(
			final OAuthResponse oAuthResponse
	) {
		if (oAuthResponse instanceof GoogleOAuthResponse googleOAuthResponse) {
			String payload = parseJws(googleOAuthResponse.idToken());
			return getGoogleOAuthMember(payload);
		}
		throw new InfraException(InfraErrorCode.INVALID_OAUTH_REQUEST);
	}

	private String parseJws(
			final String jws
	) {
		return Base64Utils.decode(jws.split("\\.")[1]);
	}

	private GoogleOAuthMember getGoogleOAuthMember(final String payload) {
		try {
			return objectMapper.readValue(payload, GoogleOAuthMember.class);
		} catch (JsonProcessingException e) {
			throw new CustomException(GlobalErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
