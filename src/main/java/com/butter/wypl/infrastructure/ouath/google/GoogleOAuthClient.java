package com.butter.wypl.infrastructure.ouath.google;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.butter.wypl.global.annotation.InfraComponent;
import com.butter.wypl.infrastructure.exception.InfraErrorCode;
import com.butter.wypl.infrastructure.exception.InfraException;
import com.butter.wypl.infrastructure.ouath.OAuthClient;
import com.butter.wypl.infrastructure.ouath.OAuthResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@InfraComponent
public class GoogleOAuthClient implements OAuthClient {

	private static final String GOOGLE_OAUTH_REQUEST_URL = "https://www.googleapis.com/oauth2/v4/token";

	private final RestTemplate restTemplate;

	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String GOOGLE_CLIENT_ID;
	@Value("${spring.security.oauth2.client.registration.google.client-secret}")
	private String GOOGLE_CLIENT_SECRET;
	@Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
	private String GOOGLE_REDIRECT_URI;

	@Override
	public OAuthResponse getOAuthMember(
			final String code
	) {
		ResponseEntity<GoogleOAuthResponse> googleOAuthResponseResponseEntity = restTemplate.postForEntity(
				GOOGLE_OAUTH_REQUEST_URL,
				getRequestParams(code),
				GoogleOAuthResponse.class);

		if (googleOAuthResponseResponseEntity.getStatusCode() == HttpStatus.OK) {
			return googleOAuthResponseResponseEntity.getBody();
		}

		throw new InfraException(InfraErrorCode.INVALID_OAUTH_REQUEST);
	}

	private HashMap<String, String> getRequestParams(final String code) {
		HashMap<String, String> params = new HashMap<>();
		params.put("code", code);
		params.put("client_id", GOOGLE_CLIENT_ID);
		params.put("client_secret", GOOGLE_CLIENT_SECRET);
		params.put("redirect_uri", GOOGLE_REDIRECT_URI);
		params.put("grant_type", "authorization_code");
		return params;
	}
}
