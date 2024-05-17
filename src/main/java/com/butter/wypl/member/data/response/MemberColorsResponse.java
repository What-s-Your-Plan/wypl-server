package com.butter.wypl.member.data.response;

import java.util.List;

import com.butter.wypl.global.common.Color;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberColorsResponse(
		List<Color> colors,
		@JsonProperty("color_count")
		int colorCount,
		@JsonProperty("select_color")
		Color selectColor
) {

	public static MemberColorsResponse of(final Color color, final List<Color> colors) {
		return new MemberColorsResponse(colors, colors.size(), color);
	}
}
