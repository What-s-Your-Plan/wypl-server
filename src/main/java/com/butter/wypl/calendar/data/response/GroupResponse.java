package com.butter.wypl.calendar.data.response;

import com.butter.wypl.global.common.Color;
import com.butter.wypl.group.domain.Group;
import com.butter.wypl.group.domain.MemberGroup;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GroupResponse(

		@JsonProperty("group_id")
		int groupId,

		Color color,

		String title
) {

	public static GroupResponse from(MemberGroup memberGroup) {
		Group group = memberGroup.getGroup();
		return new GroupResponse(group.getId(), memberGroup.getColor(), group.getName());
	}
}
