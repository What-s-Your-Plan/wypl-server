package com.butter.wypl.group.data.response;

import java.util.List;

import com.butter.wypl.global.common.Color;
import com.butter.wypl.group.domain.GroupInviteState;
import com.butter.wypl.group.domain.MemberGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record FindGroupMembersResponse(
		@JsonProperty("member_count")
		int memberCount,
		List<FindGroupMember> members,
		Color color
) {
	public static FindGroupMembersResponse of(
			final List<FindGroupMember> members,
			final Color color
	) {
		return FindGroupMembersResponse.builder()
				.memberCount(members.size())
				.members(members)
				.color(color)
				.build();
	}

	@Builder
	public record FindGroupMember(
			int id,
			String email,
			String nickname,
			@JsonInclude(JsonInclude.Include.NON_NULL)
			@JsonProperty("profile_image")
			String profileImage,
			@JsonProperty("is_accepted")
			boolean isAccepted
	) {
		public static FindGroupMember from(final MemberGroup memberGroup) {
			return FindGroupMember.builder()
					.id(memberGroup.getMember().getId())
					.email(memberGroup.getMember().getEmail())
					.nickname(memberGroup.getMember().getNickname())
					.profileImage(memberGroup.getMember().getProfileImage())
					.isAccepted(memberGroup.getGroupInviteState().equals(GroupInviteState.ACCEPTED))
					.build();
		}
	}
}
