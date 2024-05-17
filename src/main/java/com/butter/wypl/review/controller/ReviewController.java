package com.butter.wypl.review.controller;

import java.time.LocalDate;

import org.apache.http.HttpStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.butter.wypl.auth.annotation.Authenticated;
import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.global.common.Message;
import com.butter.wypl.review.data.request.ReviewCreateRequest;
import com.butter.wypl.review.data.request.ReviewType;
import com.butter.wypl.review.data.request.ReviewUpdateRequest;
import com.butter.wypl.review.data.response.ReviewDetailResponse;
import com.butter.wypl.review.data.response.ReviewIdResponse;
import com.butter.wypl.review.data.response.ReviewListResponse;
import com.butter.wypl.review.service.ReviewModifyService;
import com.butter.wypl.review.service.ReviewReadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review/v1/reviews")
public class ReviewController {

	private final ReviewModifyService reviewModifyService;
	private final ReviewReadService reviewReadService;

	@PostMapping
	public ResponseEntity<Message<ReviewIdResponse>> createReview(
		@Authenticated AuthMember authMember,
		@RequestBody ReviewCreateRequest reviewCreateRequest
	) {
		return ResponseEntity.status(HttpStatus.SC_CREATED)
			.body(
				Message.withBody("리뷰 등록에 성공했습니다.",
					reviewModifyService.createReview(authMember.getId(), reviewCreateRequest))
			);
	}

	@PatchMapping("/{reviewId}")
	public ResponseEntity<Message<ReviewIdResponse>> updateReview(
		@Authenticated AuthMember authMember,
		@PathVariable int reviewId,
		@RequestBody ReviewUpdateRequest reviewUpdateRequest
	) {
		return ResponseEntity.ok(
			Message.withBody("리뷰 수정에 성공했습니다.",
				reviewModifyService.updateReview(authMember.getId(), reviewId, reviewUpdateRequest))
		);
	}

	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Message<ReviewIdResponse>> deleteReview(
		@Authenticated AuthMember authMember,
		@PathVariable("reviewId") int reviewId
	) {
		return ResponseEntity.ok(
			Message.withBody("리뷰 삭제에 성공 했습니다.", reviewModifyService.deleteReview(authMember.getId(), reviewId))
		);
	}

	@GetMapping("/detail/{reviewId}")
	public ResponseEntity<Message<ReviewDetailResponse>> getDetailReview(
		@Authenticated AuthMember authMember,
		@PathVariable int reviewId
	) {
		return ResponseEntity.ok(
			Message.withBody("리뷰 상세 조회에 성공했습니다.", reviewReadService.getDetailReview(authMember.getId(), reviewId))
		);
	}

	@GetMapping("/{type}")
	public ResponseEntity<Message<ReviewListResponse>> getReviewsByMemberId(
		@Authenticated AuthMember authMember,
		@PathVariable("type") ReviewType reviewType,
		@RequestParam(value = "lastReviewId", required = false) Integer lastReviewId,
		@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
		@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
	) {
		return ResponseEntity.ok(
			Message.withBody("리뷰 목록 조회에 성공했습니다.",
				reviewReadService.getReviews(authMember.getId(), lastReviewId, reviewType, startDate, endDate))
		);
	}

	@GetMapping("/{type}/{scheduleId}")
	public ResponseEntity<Message<ReviewListResponse>> getReviewsBySchedule(
		@Authenticated AuthMember authMember,
		@PathVariable("type") ReviewType reviewType,
		@PathVariable("scheduleId") int scheduleId
	) {
		return ResponseEntity.ok(
			Message.withBody("일정 별 리뷰 조회에 성공했습니다.",
				reviewReadService.getReviewsByScheduleId(authMember.getId(), scheduleId, reviewType))
		);
	}
}
