package com.butter.wypl.review.data.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReviewListResponse(

	@JsonProperty("review_count")
	int reviewCount,

	List<ReviewResponse> reviews
) {

	public static ReviewListResponse from(List<ReviewResponse> reviews) {
		return new ReviewListResponse(reviews.size(), reviews);
	}
}
