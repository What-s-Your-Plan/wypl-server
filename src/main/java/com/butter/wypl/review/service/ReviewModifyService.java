package com.butter.wypl.review.service;

import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.review.data.request.ReviewCreateRequest;
import com.butter.wypl.review.data.request.ReviewUpdateRequest;
import com.butter.wypl.review.data.response.ReviewIdResponse;

public interface ReviewModifyService {
	@Transactional
	ReviewIdResponse createReview(int memberId, ReviewCreateRequest reviewCreateRequest);

	@Transactional
	ReviewIdResponse updateReview(int memberId, int reviewId, ReviewUpdateRequest reviewUpdateRequest);

	@Transactional
	ReviewIdResponse deleteReview(int memberId, int reviewId);
}
