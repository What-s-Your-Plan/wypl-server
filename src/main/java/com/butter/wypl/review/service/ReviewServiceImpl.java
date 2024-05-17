package com.butter.wypl.review.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.review.data.request.ReviewCreateRequest;
import com.butter.wypl.review.data.request.ReviewType;
import com.butter.wypl.review.data.request.ReviewUpdateRequest;
import com.butter.wypl.review.data.response.ReviewDetailResponse;
import com.butter.wypl.review.data.response.ReviewIdResponse;
import com.butter.wypl.review.data.response.ReviewListResponse;
import com.butter.wypl.review.data.response.ReviewResponse;
import com.butter.wypl.review.domain.Review;
import com.butter.wypl.review.domain.ReviewContents;
import com.butter.wypl.review.exception.ReviewErrorCode;
import com.butter.wypl.review.exception.ReviewException;
import com.butter.wypl.review.repository.ReviewContentsRepository;
import com.butter.wypl.review.repository.ReviewRepository;
import com.butter.wypl.schedule.domain.MemberSchedule;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.respository.ScheduleRepository;
import com.butter.wypl.schedule.service.MemberScheduleService;
import com.butter.wypl.schedule.utils.ScheduleServiceUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewReadService, ReviewModifyService {

	private final ReviewRepository reviewRepository;

	private final MemberScheduleService memberScheduleService;

	private final ScheduleRepository scheduleRepository;

	private final ReviewContentsRepository reviewContentsRepository;

	@Override
	@Transactional
	public ReviewIdResponse createReview(int memberId, ReviewCreateRequest reviewCreateRequest) {
		validateReviewContents(reviewCreateRequest.contents());

		//schedule, member 유효성 판단
		MemberSchedule memberSchedule = memberScheduleService.getMemberScheduleByMemberAndSchedule(memberId,
				ScheduleServiceUtils.findById(scheduleRepository, reviewCreateRequest.scheduleId()));
		memberSchedule.writeReview();

		Review savedReview = reviewRepository.save(Review.of(reviewCreateRequest, memberSchedule));

		reviewContentsRepository.save(
				ReviewContents.of(savedReview.getReviewId(), reviewCreateRequest.contents()));

		return ReviewIdResponse.from(savedReview.getReviewId());
	}

	@Override
	@Transactional
	public ReviewIdResponse updateReview(int memberId, int reviewId, ReviewUpdateRequest reviewUpdateRequest) {
		validateReviewContents(reviewUpdateRequest.contents());
		Review review = reviewRepository.getByReviewId(reviewId);

		//유효성 검사
		review.validationOwnerByMemberId(memberId);

		review.updateTitle(reviewUpdateRequest.title());

		ReviewContents reviewContents = reviewContentsRepository.findByReviewIdAndDeletedAtNull(
				review.getReviewId());
		reviewContents.updateContents(reviewUpdateRequest.contents());

		reviewContentsRepository.save(reviewContents);

		return ReviewIdResponse.from(review.getReviewId());
	}

	@Override
	@Transactional
	public ReviewIdResponse deleteReview(int memberId, int reviewId) {
		Review review = reviewRepository.getByReviewId(reviewId);

		//유효성 검사
		review.validationOwnerByMemberId(memberId);

		ReviewContents reviewContents = reviewContentsRepository.findByReviewIdAndDeletedAtNull(
				review.getReviewId());
		reviewContents.delete();

		reviewContentsRepository.save(reviewContents);

		review.delete();

		return ReviewIdResponse.from(review.getReviewId());
	}

	@Override
	public ReviewDetailResponse getDetailReview(int memberId, int reviewId) {
		Review review = reviewRepository.getByReviewId(reviewId);

		//유효성 검사
		review.validationOwnerByMemberId(memberId);

		ReviewContents reviewContents = reviewContentsRepository.findByReviewIdAndDeletedAtNull(
				review.getReviewId());

		Schedule schedule = review.getMemberSchedule().getSchedule();

		return ReviewDetailResponse.of(review, schedule,
				memberScheduleService.getMembersBySchedule(schedule),
				reviewContents == null ? null : reviewContents.getContents());
	}

	@Override
	public ReviewListResponse getReviews(int memberId, Integer lastReviewId, ReviewType reviewType, LocalDate startDate,
			LocalDate endDate) {

		List<Review> reviews = switch (reviewType) {
			case NEWEST -> {
				if (startDate == null || endDate == null) {
					if (lastReviewId == null) {
						yield reviewRepository.getReviewsNewestAll(memberId);
					}
					yield reviewRepository.getReviewsNewestAllAfter(memberId, lastReviewId);
				}

				LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(0, 0));
				LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(23, 59));

				if (lastReviewId == null) {
					yield reviewRepository.getReviewsNewest(memberId, startDateTime, endDateTime);
				}

				yield reviewRepository.getReviewsNewestAfter(memberId, lastReviewId, startDateTime, endDateTime);
			}
			case OLDEST -> {

				if (startDate == null || endDate == null) {
					if (lastReviewId == null) {
						lastReviewId = 0;
					}
					yield reviewRepository.getReviewsOldestAll(memberId, lastReviewId);
				}
				LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(0, 0));
				LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(23, 59));

				if (lastReviewId == null) {
					lastReviewId = 0;
				}

				yield reviewRepository.getReviewsOldest(memberId, lastReviewId, startDateTime, endDateTime);
			}
		};

		return ReviewListResponse.from(reviews.stream().map(
				review -> ReviewResponse.builder()
						.title(review.getTitle())
						.createdAt(review.getCreatedAt())
						.reviewId(review.getReviewId())
						.thumbnailContent(getThumbnailContent(
								reviewContentsRepository.findByReviewIdAndDeletedAtNull(review.getReviewId())
										.getContents()))
						.build()
		).toList());
	}

	@Override
	public ReviewListResponse getReviewsByScheduleId(int memberId, int scheduleId, ReviewType reviewType) {
		MemberSchedule memberSchedule = memberScheduleService.getMemberScheduleByMemberAndSchedule(memberId,
				ScheduleServiceUtils.findById(scheduleRepository, scheduleId));

		List<Review> reviews = switch (reviewType) {
			case NEWEST -> {
				yield reviewRepository.getReviewsByMemberScheduleOrderByCreatedAtDesc(memberSchedule);
			}
			case OLDEST -> {
				yield reviewRepository.getReviewsByMemberScheduleOrderByCreatedAt(memberSchedule);
			}
		};

		List<ReviewResponse> reviewResponses = new ArrayList<>();
		for (Review review : reviews) {
			reviewResponses.add(
					ReviewResponse.builder()
							.reviewId(review.getReviewId())
							.title(review.getTitle())
							.createdAt(review.getCreatedAt())
							.thumbnailContent(
									getThumbnailContent(
											reviewContentsRepository.findByReviewIdAndDeletedAtNull(
													review.getReviewId()).getContents())
							)
							.build()
			);
		}

		return ReviewListResponse.from(reviewResponses);
	}

	private Map<String, Object> getThumbnailContent(List<Map<String, Object>> contents) {
		for (Map<String, Object> content : contents) {
			if (content.get("blockType").equals("image")) {
				return content;
			}
		}

		return contents.get(0);
	}

	private void validateReviewContents(List<Map<String, Object>> contents) {
		if (contents.isEmpty()) {
			throw new ReviewException(ReviewErrorCode.EMPTY_CONTENTS);
		}
	}
}
