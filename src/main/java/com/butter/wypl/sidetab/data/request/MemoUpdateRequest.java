package com.butter.wypl.sidetab.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MemoUpdateRequest(
		@JsonProperty("memo")
		String memo
) {
}
