package com.butter.wypl.auth.data;

import lombok.Builder;

@Builder
public record JsonWebTokens(
		String accessToken,
		String refreshToken
) {
}
