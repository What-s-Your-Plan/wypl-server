package com.butter.wypl.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.butter.wypl.auth.annotation.Authenticated;
import com.butter.wypl.auth.data.response.AuthTokensResponse;
import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.auth.service.AuthService;
import com.butter.wypl.global.common.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

	private final AuthService authService;

	@PostMapping("/v1/sign-in/{provider}")
	public ResponseEntity<Message<AuthTokensResponse>> signIn(
			@PathVariable("provider") String provider,
			@RequestParam("code") String code
	) {
		AuthTokensResponse response = authService.generateTokens(provider, code);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Message.withBody("로그인에 성공하였습니다.", response));
	}

	@PutMapping("/v1/reissue")
	public ResponseEntity<Message<AuthTokensResponse>> reissue(
			@RequestParam("refresh_token") String refreshToken
	) {
		AuthTokensResponse response = authService.reissueTokens(refreshToken);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Message.withBody("토큰 재발급에 성공하였습니다.", response));
	}

	@DeleteMapping("/v1/logout")
	public ResponseEntity<Message<Void>> logout(
			@Authenticated AuthMember authMember
	) {
		authService.deleteToken(authMember);
		return ResponseEntity.ok(Message.onlyMessage("로그아웃에 성공하였습니다."));
	}
}
