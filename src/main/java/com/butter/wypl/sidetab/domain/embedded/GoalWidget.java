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
public class GoalWidget {

	@Column(name = "goal", length = 60)
	private String value;

	private GoalWidget(final String value) {
		this.value = value;
	}

	public static GoalWidget from(final String value) {
		validate(value);
		return new GoalWidget(value);
	}

	private static void validate(final String value) {
		if (value == null) {
			return;
		}
		if (value.length() > 60) {
			throw new MemberException(MemberErrorCode.TOO_LONG_CONTENT);
		}
	}
}
