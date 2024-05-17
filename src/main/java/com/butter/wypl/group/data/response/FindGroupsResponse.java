package com.butter.wypl.group.data.response;

import java.util.List;

import com.butter.wypl.global.common.Color;
import com.butter.wypl.group.domain.MemberGroup;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record FindGroupsResponse(
		@JsonProperty("group_count")
		int groupCount,
		List<FindGroup> groups,
		@JsonProperty("invited_group_count")
		int invitedGroupCount,
		@JsonProperty("invited_groups")
		List<FindGroup> invitedGroups
) {

	public static FindGroupsResponse of(
			final List<FindGroup> groups,
			final List<FindGroup> invitedGroups
	) {
		return FindGroupsResponse.builder()
				.groupCount(groups.size())
				.groups(groups)
				.invitedGroupCount(invitedGroups.size())
				.invitedGroups(invitedGroups)
				.build();
	}

	@Builder
	public record FindGroup(
			@JsonProperty("id")
			int id,
			@JsonProperty("name")
			String name,
			@JsonProperty("color")
			Color color,
			@JsonProperty("is_owner")
			boolean isOwner
	) {
		public static FindGroup from(
				final MemberGroup memberGroup
		) {
			return FindGroup.builder()
					.id(memberGroup.getGroup().getId())
					.name(memberGroup.getGroup().getName())
					.color(memberGroup.getColor())
					.isOwner(memberGroup.isOwner())
					.build();
		}
	}
}
