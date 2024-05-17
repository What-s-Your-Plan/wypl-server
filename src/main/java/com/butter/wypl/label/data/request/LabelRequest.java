package com.butter.wypl.label.data.request;

import com.butter.wypl.global.common.Color;

public record LabelRequest(
	String title,
	Color color
) {
}
