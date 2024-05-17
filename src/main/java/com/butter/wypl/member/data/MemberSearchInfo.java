package com.butter.wypl.member.data;

import com.butter.wypl.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record MemberSearchInfo(
		@JsonProperty("id")
		int id,
		@JsonProperty("email")
		String email,
		@JsonProperty("nickname")
		String nickname,
		@JsonInclude(JsonInclude.Include.NON_NULL)
		@JsonProperty("profile_image_url")
		String profileImage
) {

	public static MemberSearchInfo from(final Member member) {
		return MemberSearchInfo.builder()
				.id(member.getId())
				.email(member.getEmail())
				.nickname(member.getNickname())
				.profileImage(member.getProfileImage())
				.build();
	}
}
