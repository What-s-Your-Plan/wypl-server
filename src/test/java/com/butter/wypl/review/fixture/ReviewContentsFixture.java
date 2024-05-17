package com.butter.wypl.review.fixture;

import java.util.List;
import java.util.Map;

import com.butter.wypl.review.domain.ReviewContents;

public enum ReviewContentsFixture {
	REVIEW_CONTENTS(1,
		List.of(
			Map.of(
				"blockType", "emotion",
				"emoji", "/src/assets/icons/emoji/tired.svg",
				"description", "피곤해요"
			),
			Map.of(
				"blockType", "4f",
				"weather", "/src/assets/icons/weather/sun.svg",
				"description", "날씨 너무 좋음"
			),
			Map.of(
				"blockType", "4f",
				"facts", "팩트",
				"feelings", "느낌",
				"finding", "교훈",
				"future", "향후 헹동"
			)
		)),
	REVIEW_CONTENTS2(2,
		List.of(
			Map.of(
				"blockType", "emotion",
				"emoji", "/src/assets/icons/emoji/tired.svg",
				"description", "피곤해요"
			),
			Map.of(
				"blockType", "4f",
				"weather", "/src/assets/icons/weather/sun.svg",
				"description", "날씨 너무 좋음"
			)
		)),
	;
	private final int reviewId;

	private final List<Map<String, Object>> contents;

	ReviewContentsFixture(int reviewId, List<Map<String, Object>> contents) {
		this.reviewId = reviewId;
		this.contents = contents;
	}

	public ReviewContents toReviewContents() {
		return ReviewContents.builder()
			.reviewId(reviewId)
			.contents(contents)
			.build();
	}
}
