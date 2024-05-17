package com.butter.wypl.member.data.response;

import java.util.List;

import com.butter.wypl.member.data.MemberSearchInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberSearchResponse(
		@JsonProperty("members")
		List<MemberSearchInfo> members,
		@JsonProperty("member_count")
		int count
) {

	public static MemberSearchResponse from(final List<MemberSearchInfo> members) {
		return new MemberSearchResponse(members, members.size());
	}
}
