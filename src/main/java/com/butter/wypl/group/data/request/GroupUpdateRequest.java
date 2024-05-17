package com.butter.wypl.group.data.request;

import com.butter.wypl.global.common.Color;

public record GroupUpdateRequest(
	String name,
	Color color
) {

}
