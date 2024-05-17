package com.butter.wypl.member.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberProfileImageUpdateResponse(
		@JsonProperty("profile_image_url")
		String profileImageUrl
) {
}
