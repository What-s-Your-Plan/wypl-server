package com.butter.wypl.auth.mock;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.butter.wypl.auth.data.response.AuthTokensResponse;
import com.butter.wypl.global.annotation.Generated;
import com.butter.wypl.global.common.Message;

import lombok.RequiredArgsConstructor;

@Generated
@Profile({"local", "dev"})
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class MockAuthController {

	private final MockAuthService authService;

	@PostMapping("/v1/sign-in/mock")
	public ResponseEntity<Message<AuthTokensResponse>> mockIssueToken(
			@RequestParam("email") String email
	) {
		AuthTokensResponse response = authService.generateTokens(email + "@gmail.com");
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Message.withBody("로그인에 성공하였습니다.", response));
	}
}
