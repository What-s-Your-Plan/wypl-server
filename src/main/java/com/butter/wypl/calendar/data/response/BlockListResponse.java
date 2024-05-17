package com.butter.wypl.calendar.data.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BlockListResponse(
	@JsonProperty("block_count")
	int blockCount,

	List<BlockResponse> blocks
) {

	public static BlockListResponse from(List<BlockResponse> blockResponses) {
		return new BlockListResponse(
			blockResponses.size(),
			blockResponses
		);
	}
}
