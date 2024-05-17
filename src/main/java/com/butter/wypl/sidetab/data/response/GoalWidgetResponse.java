package com.butter.wypl.sidetab.data.response;

import com.butter.wypl.sidetab.domain.SideTab;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GoalWidgetResponse(
		@JsonProperty("goal_id")
		int goalId,
		@JsonProperty("content")
		String content
) {
	public static GoalWidgetResponse from(SideTab findSideTab) {
		return new GoalWidgetResponse(findSideTab.getId(), findSideTab.getGoal());
	}
}
