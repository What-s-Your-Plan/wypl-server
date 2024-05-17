package com.butter.wypl.calendar.data.response;

import com.butter.wypl.global.common.Color;
import com.butter.wypl.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberResponse(

	@JsonProperty("member_id")
	int memberId,

	String nickname,

	Color color
) {

	public static MemberResponse from(Member member) {
		return new MemberResponse(
			member.getId(),
			member.getNickname(),
			member.getColor()
		);
	}
}
