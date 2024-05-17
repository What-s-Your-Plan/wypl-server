package com.butter.wypl.label.data.response;

import com.butter.wypl.global.common.Color;
import com.butter.wypl.group.domain.Group;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.label.data.Category;
import com.butter.wypl.label.domain.Label;

public record AllLabelResponse(

	int id,

	Category category,

	String title,

	Color color
) {

	public static AllLabelResponse from(Label label) {
		return new AllLabelResponse(
			label.getLabelId(),
			Category.MEMBER,
			label.getTitle(),
			label.getColor()
		);
	}

	public static AllLabelResponse of(MemberGroup memberGroup, Group group) {
		return new AllLabelResponse(
			group.getId(),
			Category.GROUP,
			group.getName(),
			memberGroup.getColor()
		);
	}
}
