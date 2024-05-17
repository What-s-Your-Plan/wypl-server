package com.butter.wypl.sidetab.data.response;

import com.butter.wypl.sidetab.domain.embedded.MemoWidget;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MemoWidgetResponse(
		@JsonProperty("memo")
		String memo
) {
	public static MemoWidgetResponse from(String memo) {
		return new MemoWidgetResponse(memo);
	}
}
