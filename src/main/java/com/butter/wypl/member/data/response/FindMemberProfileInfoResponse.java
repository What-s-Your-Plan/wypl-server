package com.butter.wypl.member.data.response;

import com.butter.wypl.global.common.Color;
import com.butter.wypl.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FindMemberProfileInfoResponse(
		@JsonProperty("id")
		int id,
		@JsonProperty("email")
		String email,
		@JsonProperty("nickname")
		String nickname,
		@JsonInclude(JsonInclude.Include.NON_NULL)
		@JsonProperty("profile_image_url")
		String profileImage,
		@JsonProperty("main_color")
		Color mainColor
) {

	public static FindMemberProfileInfoResponse from(final Member member) {
		return new FindMemberProfileInfoResponse(
				member.getId(),
				member.getEmail(),
				member.getNickname(),
				member.getProfileImage(),
				member.getColor()
		);
	}
}
