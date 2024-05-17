package com.butter.wypl.review.data.response;

import java.util.List;
import java.util.Map;

import com.butter.wypl.member.domain.Member;
import com.butter.wypl.review.domain.Review;
import com.butter.wypl.schedule.data.response.ScheduleResponse;
import com.butter.wypl.schedule.domain.Schedule;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record ReviewDetailResponse(
	@JsonProperty("review_id")
	int reviewId,

	String title,

	ScheduleResponse schedule,

	List<Map<String, Object>> contents
) {

	public static ReviewDetailResponse of(Review review, Schedule schedule, List<Member> members,
		List<Map<String, Object>> reviewContents) {
		return ReviewDetailResponse.builder()
			.reviewId(review.getReviewId())
			.title(review.getTitle())
			.schedule(ScheduleResponse.of(schedule, members))
			.contents(reviewContents)
			.build();
	}
}
