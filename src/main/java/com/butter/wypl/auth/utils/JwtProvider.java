package com.butter.wypl.auth.utils;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.butter.wypl.auth.data.JsonWebTokens;
import com.butter.wypl.auth.exception.AuthErrorCode;
import com.butter.wypl.auth.exception.AuthException;
import com.butter.wypl.global.annotation.Generated;
import com.butter.wypl.global.utils.Base64Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
	private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;
	private static final long REFRESH_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L;

	private final Key accessKey;
	private final Key refreshKey;
	private final ObjectMapper objectMapper;

	@Autowired
	public JwtProvider(
			@Value("${jwt.access-key}") String accessKey,
			@Value("${jwt.refresh-key}") String refreshKey,
			ObjectMapper objectMapper
	) {
		this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
		this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
		this.objectMapper = objectMapper;
	}

	public JsonWebTokens generateJsonWebTokens(
			final int memberId
	) {
		long now = System.currentTimeMillis();

		String accessToken = generateToken(
				new Date(now + ACCESS_TOKEN_EXPIRE_TIME),
				accessKey,
				memberId,
				"accessToken");
		String refreshToken = generateToken(
				new Date(now + REFRESH_TOKEN_EXPIRE_TIME),
				refreshKey,
				memberId,
				"refreshToken"
		);

		return JsonWebTokens.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
	}

	private String generateToken(
			final Date expireTime,
			final Key key,
			final int memberId,
			final String tokenType
	) {
		return Jwts.builder()
				.setExpiration(expireTime)
				.claim("member_id", memberId)
				.claim("type", tokenType)
				.claim("expire_time", expireTime.getTime())
				.signWith(key, SignatureAlgorithm.HS512)
				.compact();
	}

	@Generated
	public void validateToken(
			final String token
	) {
		String payload = parsePayload(token);
		String type = getTokenType(payload);
		try {
			if (type.equals("accessToken")) {
				Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(token);
				return;
			}
			if (type.equals("refreshToken")) {
				Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(token);
				return;
			}
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			throw new AuthException(AuthErrorCode.WRONG_TYPE_TOKEN);
		} catch (ExpiredJwtException e) {
			throw new AuthException(AuthErrorCode.EXPIRED_TOKEN);
		} catch (UnsupportedJwtException | IllegalArgumentException e) {
			throw new AuthException(AuthErrorCode.UNSUPPORTED_TOKEN);
		}
		throw new AuthException(AuthErrorCode.INVALID_JWT);
	}

	private String parsePayload(final String token) {
		try {
			return token.split("\\.")[1];
		} catch (RuntimeException e) {
			throw new AuthException(AuthErrorCode.INVALID_JWT);
		}
	}

	private String getTokenType(final String payload) {
		try {
			return objectMapper.readValue(Base64Utils.decode(payload), TokenType.class).type();
		} catch (JsonProcessingException e) {
			throw new AuthException(AuthErrorCode.INVALID_JWT);
		}
	}

	public int getPayload(final String token) {
		validateToken(token);
		return Jwts.parserBuilder()
				.setSigningKey(accessKey)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.get("member_id", Integer.class);
	}

	public int getPayloadByRefreshToken(final String token) {
		validateToken(token);
		return Jwts.parserBuilder()
				.setSigningKey(refreshKey)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.get("member_id", Integer.class);
	}

	private record TokenType(String type) {
	}
}
