package com.butter.wypl.schedule.data.response;

import java.util.List;

import com.butter.wypl.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberResponse(
	@JsonProperty("member_id")
	int memberId,

	String nickname,

	@JsonProperty("profile_image")
	String profileImage
) {

	public static MemberResponse from(Member member) {
		return new MemberResponse(member.getId(), member.getNickname(), member.getProfileImage());
	}

	public static List<MemberResponse> from(List<Member> members) {
		return members.stream().map(MemberResponse::from)
			.toList();
	}

}
