package com.butter.wypl.auth.domain;

import lombok.Getter;

@Getter
public class AuthMember {
	private final int id;

	private AuthMember(int id) {
		this.id = id;
	}

	public static AuthMember from(final int id) {
		return new AuthMember(id);
	}
}
