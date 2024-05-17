package com.butter.wypl.review.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.MongoRepositoryTest;
import com.butter.wypl.review.domain.ReviewContents;
import com.butter.wypl.review.fixture.ReviewContentsFixture;

@MongoRepositoryTest
public class ReviewContentsRepositoryTest {

	private final ReviewContentsRepository reviewContentsRepository;

	@Autowired
	public ReviewContentsRepositoryTest(ReviewContentsRepository reviewContentsRepository) {
		this.reviewContentsRepository = reviewContentsRepository;
	}

	@Test
	@DisplayName("리뷰 콘텐츠 저장되는지 확인")
	void saveReviewContents() {
		// Given
		ReviewContents reviewContents = ReviewContentsFixture.REVIEW_CONTENTS.toReviewContents();
		// When
		ReviewContents savedReviewContents = reviewContentsRepository.save(reviewContents);

		// Then
		assertThat(savedReviewContents).isNotNull();
		assertThat(savedReviewContents.getReviewId()).isEqualTo(reviewContents.getReviewId());
		assertThat(savedReviewContents.getContents()).isEqualTo(reviewContents.getContents());
	}

	@Test
	@DisplayName("리뷰 컨텐츠 조회")
	void getReviewContents() {
		// Given
		ReviewContents reviewContents = reviewContentsRepository.save(
			ReviewContentsFixture.REVIEW_CONTENTS2.toReviewContents());

		// When
		ReviewContents findReviewContents = reviewContentsRepository.findByReviewIdAndDeletedAtNull(
			reviewContents.getReviewId());

		// Then
		assertThat(findReviewContents.getContents()).isEqualTo(reviewContents.getContents());
		assertThat(findReviewContents.getReviewId()).isEqualTo(reviewContents.getReviewId());

	}
}
