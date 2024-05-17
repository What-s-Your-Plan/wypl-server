package com.butter.wypl.sidetab.domain.embedded;

import com.butter.wypl.member.exception.MemberErrorCode;
import com.butter.wypl.member.exception.MemberException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MemoWidget {

	@Column(name = "memo", length = 1_000)
	private String value;

	public MemoWidget(final String value) {
		this.value = value;
	}

	public static MemoWidget from(final String value) {
		validate(value);
		return new MemoWidget(value);
	}

	private static void validate(final String memo) {
		if (memo == null) {
			return;
		}
		if (memo.length() > 1_000) {
			throw new MemberException(MemberErrorCode.TOO_LONG_CONTENT);
		}
	}
}
