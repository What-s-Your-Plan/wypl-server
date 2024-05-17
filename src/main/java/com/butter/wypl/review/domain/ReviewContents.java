package com.butter.wypl.review.domain;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.butter.wypl.global.common.MongoBaseEntityWithDelete;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Document(collection = "review_contents")
@Builder
@Getter
public class ReviewContents extends MongoBaseEntityWithDelete {
	@Id
	private int reviewId;

	private List<Map<String, Object>> contents;

	public static ReviewContents of(int reviewId, List<Map<String, Object>> contents) {
		return ReviewContents.builder()
			.reviewId(reviewId)
			.contents(contents)
			.build();
	}

	public void updateContents(List<Map<String, Object>> contents) {
		this.contents = contents;
	}
}
