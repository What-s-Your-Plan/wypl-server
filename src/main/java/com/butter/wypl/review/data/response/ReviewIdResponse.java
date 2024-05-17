package com.butter.wypl.review.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReviewIdResponse(
	@JsonProperty("review_id")
	int reviewId
) {

	public static ReviewIdResponse from(int reviewId) {
		return new ReviewIdResponse(reviewId);
	}
}
