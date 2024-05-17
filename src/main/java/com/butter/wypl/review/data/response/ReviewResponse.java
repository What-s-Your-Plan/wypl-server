package com.butter.wypl.review.data.response;

import java.time.LocalDateTime;
import java.util.Map;

import com.butter.wypl.review.domain.Review;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record ReviewResponse(

	@JsonProperty("review_id")
	int reviewId,

	@JsonProperty("created_at")
	LocalDateTime createdAt,

	String title,

	@JsonProperty("thumbnail_content")
	Map<String, Object> thumbnailContent
) {

	public static ReviewResponse from(Review review, Map<String, Object> thumbnailContent) {
		return ReviewResponse.builder()
			.createdAt(LocalDateTime.now())
			.reviewId(review.getReviewId())
			.title(review.getTitle())
			.thumbnailContent(thumbnailContent)
			.build();
	}
}
