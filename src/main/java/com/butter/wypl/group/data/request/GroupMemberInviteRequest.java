package com.butter.wypl.group.data.request;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GroupMemberInviteRequest(
	@JsonProperty("member_id_list")
	Set<Integer> memberIdList
) {
}
