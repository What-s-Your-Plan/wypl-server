package com.butter.wypl.review.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.review.data.request.ReviewCreateRequest;
import com.butter.wypl.review.data.request.ReviewType;
import com.butter.wypl.review.data.request.ReviewUpdateRequest;
import com.butter.wypl.review.data.response.ReviewDetailResponse;
import com.butter.wypl.review.data.response.ReviewIdResponse;
import com.butter.wypl.review.data.response.ReviewListResponse;
import com.butter.wypl.review.domain.Review;
import com.butter.wypl.review.domain.ReviewContents;
import com.butter.wypl.review.fixture.ReviewContentsFixture;
import com.butter.wypl.review.fixture.ReviewFixture;
import com.butter.wypl.review.repository.ReviewContentsRepository;
import com.butter.wypl.review.repository.ReviewRepository;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.fixture.ScheduleFixture;
import com.butter.wypl.schedule.respository.ScheduleRepository;
import com.butter.wypl.schedule.service.MemberScheduleService;

@MockServiceTest
public class ReviewServiceTest {

	@InjectMocks
	private ReviewServiceImpl reviewService;

	@Mock
	private ReviewRepository reviewRepository;

	@Mock
	private MemberScheduleService memberScheduleService;

	@Mock
	private ScheduleRepository scheduleRepository;

	@Mock
	private ReviewContentsRepository reviewContentsRepository;

	@Test
	@DisplayName("리뷰 생성")
	void createReview() {

	}

	@Test
	@DisplayName("리뷰 수정")
	void updateReview() {

	}

	@Test
	@DisplayName("리뷰 삭제")
	void deleteReview() {

	}

	@Test
	@DisplayName("리뷰 상세 조회")
	void getDetailReview() {

	}

	@Nested
	@DisplayName("리뷰 목록 조회")
	class getReviews {

		Review review1, review2;
		ReviewContents reviewContents;

		@BeforeEach
		void init() {

		}

		@Test
		@DisplayName("지정 날짜 x, 오래된 순, 처음")
		void getReviews1() {
			// Given
			given(reviewRepository.getReviewsOldestAll(anyInt(), anyInt()))
				.willReturn(List.of(review1, review2));
			// When
			ReviewListResponse reviewListResponse = reviewService.getReviews(1, null, ReviewType.OLDEST, null, null);
			// Then
			assertThat(reviewListResponse.reviewCount()).isEqualTo(2);
		}

		@Test
		@DisplayName("지정 날짜 x, 오래된 순, 처음 이후")
		void getReviews2() {
			// Given
			given(reviewRepository.getReviewsOldestAll(anyInt(), anyInt()))
				.willReturn(List.of(review1, review2));
			// When
			ReviewListResponse reviewListResponse = reviewService.getReviews(1, 1, ReviewType.OLDEST, null, null);
			// Then
			assertThat(reviewListResponse.reviewCount()).isEqualTo(2);

		}

		@Test
		@DisplayName("지정 날짜 x, 최신 순, 처음")
		void getReviews3() {
			// Given
			given(reviewRepository.getReviewsNewestAll(anyInt()))
				.willReturn(List.of(review1, review2));
			// When
			ReviewListResponse reviewListResponse = reviewService.getReviews(1, null, ReviewType.NEWEST, null, null);
			// Then
			assertThat(reviewListResponse.reviewCount()).isEqualTo(2);

		}

		@Test
		@DisplayName("지정 날짜 x, 최신 순, 처음 이후")
		void getReviews4() {
			//Given
			given(reviewRepository.getReviewsNewestAllAfter(anyInt(), anyInt()))
				.willReturn(List.of(review1, review2));
			// When
			ReviewListResponse reviewListResponse = reviewService.getReviews(1, 1, ReviewType.NEWEST, null, null);
			// Then
			assertThat(reviewListResponse.reviewCount()).isEqualTo(2);
		}

		@Test
		@DisplayName("지정 날짜 O, 오래된 순, 처음")
		void getReviews5() {
			// Given
			given(reviewRepository.getReviewsOldest(anyInt(), anyInt(), any(LocalDateTime.class),
				any(LocalDateTime.class)))
				.willReturn(List.of(review1, review2));

			// When
			ReviewListResponse reviewListResponse = reviewService.getReviews(1, null, ReviewType.OLDEST,
				LocalDate.of(2024, 5, 9), LocalDate.of(2024, 5, 10));

			// Then
			assertThat(reviewListResponse.reviewCount()).isEqualTo(2);

		}

		@Test
		@DisplayName("지정 날짜 O, 오래된 순, 처음 이후")
		void getReviews6() {
			// Given
			given(reviewRepository.getReviewsOldest(anyInt(), anyInt(), any(LocalDateTime.class),
				any(LocalDateTime.class)))
				.willReturn(List.of(review1, review2));

			// When
			ReviewListResponse reviewListResponse = reviewService.getReviews(1, 1, ReviewType.OLDEST,
				LocalDate.of(2024, 5, 9), LocalDate.of(2024, 5, 10));

			// Then
			assertThat(reviewListResponse.reviewCount()).isEqualTo(2);

		}

		@Test
		@DisplayName("지정 날짜 O, 최신 순, 처음")
		void getReviews7() {
			// Given
			given(reviewRepository.getReviewsNewest(anyInt(), any(LocalDateTime.class),
				any(LocalDateTime.class)))
				.willReturn(List.of(review1, review2));

			// When
			ReviewListResponse reviewListResponse = reviewService.getReviews(1, null, ReviewType.NEWEST,
				LocalDate.of(2024, 5, 9), LocalDate.of(2024, 5, 10));

			// Then
			assertThat(reviewListResponse.reviewCount()).isEqualTo(2);

		}

		@Test
		@DisplayName("지정 날짜 O, 최신, 처음 이후")
		void getReviews8() {
			// Given
			given(reviewRepository.getReviewsNewestAfter(anyInt(), anyInt(), any(LocalDateTime.class),
				any(LocalDateTime.class)))
				.willReturn(List.of(review1, review2));

			// When
			ReviewListResponse reviewListResponse = reviewService.getReviews(1, 1, ReviewType.NEWEST,
				LocalDate.of(2024, 5, 9), LocalDate.of(2024, 5, 10));

			// Then
			assertThat(reviewListResponse.reviewCount()).isEqualTo(2);

		}
	}

	@Nested
	@DisplayName("일정 별 리뷰 목록 조회")
	class getReviewsBySchedule {

	}
}
