package com.butter.wypl.group.data.request;

import java.util.Set;

import com.butter.wypl.global.common.Color;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GroupCreateRequest(
	String name,
	Color color,
	@JsonProperty("member_id_list")
	Set<Integer> memberIdList

) {

}
