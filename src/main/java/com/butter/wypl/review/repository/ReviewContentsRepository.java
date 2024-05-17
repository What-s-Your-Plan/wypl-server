package com.butter.wypl.review.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.butter.wypl.review.domain.ReviewContents;

public interface ReviewContentsRepository extends MongoRepository<ReviewContents, String> {

	ReviewContents findByReviewIdAndDeletedAtNull(int reviewId);
}
