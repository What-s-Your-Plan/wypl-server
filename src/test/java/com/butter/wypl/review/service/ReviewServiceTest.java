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
import com.butter.wypl.schedule.domain.MemberSchedule;
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
		// Given
		MemberSchedule memberSchedule = MemberSchedule.builder()
			.member(MemberFixture.JWA_SO_YEON.toMember())
			.schedule(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule())
			.build();
		Review review = ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule);
		ReviewContents reviewContents = ReviewContentsFixture.REVIEW_CONTENTS.toReviewContents();

		given(memberScheduleService.getMemberScheduleByMemberAndSchedule(anyInt(), any(Schedule.class)))
			.willReturn(memberSchedule);
		given(reviewRepository.save(any(Review.class)))
			.willReturn(ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule));
		given(reviewContentsRepository.save(any(ReviewContents.class)))
			.willReturn(reviewContents);
		given(scheduleRepository.findById(anyInt()))
			.willReturn(Optional.of(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule()));

		// When
		ReviewIdResponse response = reviewService.createReview(1,
			ReviewCreateRequest.builder()
				.title(review.getTitle())
				.scheduleId(1)
				.contents(reviewContents.getContents())
				.build()
		);

		// Then
		assertThat(response.reviewId()).isEqualTo(review.getReviewId());
	}

	@Test
	@DisplayName("리뷰 수정")
	void updateReview() {
		// Given
		MemberSchedule memberSchedule = MemberSchedule.builder()
			.member(MemberFixture.JWA_SO_YEON.toMemberWithId(1))
			.schedule(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule())
			.build();
		Review review = ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule);
		ReviewContents reviewContents = ReviewContentsFixture.REVIEW_CONTENTS.toReviewContents();

		given(reviewContentsRepository.save(any(ReviewContents.class)))
			.willReturn(reviewContents);
		given(reviewContentsRepository.findByReviewIdAndDeletedAtNull(anyInt()))
			.willReturn(reviewContents);
		given(reviewRepository.getByReviewId(anyInt()))
			.willReturn(review);

		// When
		ReviewIdResponse response = reviewService.updateReview(1, 1,
			ReviewUpdateRequest.builder()
				.title("바뀐제목")
				.scheduleId(1)
				.contents(reviewContents.getContents())
				.build()
		);

		//Then
		assertThat(response.reviewId()).isEqualTo(review.getReviewId());
	}

	@Test
	@DisplayName("리뷰 삭제")
	void deleteReview() {
		// Given
		MemberSchedule memberSchedule = MemberSchedule.builder()
			.member(MemberFixture.JWA_SO_YEON.toMemberWithId(1))
			.schedule(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule())
			.build();
		Review review = ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule);
		ReviewContents reviewContents = ReviewContentsFixture.REVIEW_CONTENTS.toReviewContents();

		given(reviewRepository.getByReviewId(anyInt()))
			.willReturn(review);
		given(reviewContentsRepository.findByReviewIdAndDeletedAtNull(anyInt()))
			.willReturn(reviewContents);

		// When
		ReviewIdResponse response = reviewService.deleteReview(1, 1);
		// Then
		assertThat(response.reviewId()).isEqualTo(review.getReviewId());
	}

	@Test
	@DisplayName("리뷰 상세 조회")
	void getDetailReview() {
		// Given
		MemberSchedule memberSchedule = MemberSchedule.builder()
			.member(MemberFixture.JWA_SO_YEON.toMemberWithId(1))
			.schedule(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule())
			.build();
		Review review = ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule);
		ReviewContents reviewContents = ReviewContentsFixture.REVIEW_CONTENTS.toReviewContents();

		given(reviewRepository.getByReviewId(anyInt()))
			.willReturn(review);
		given(reviewContentsRepository.findByReviewIdAndDeletedAtNull(anyInt()))
			.willReturn(reviewContents);

		// When
		ReviewDetailResponse response = reviewService.getDetailReview(1, 1);
		// Then
		assertThat(response.reviewId()).isEqualTo(review.getReviewId());
	}

	@Nested
	@DisplayName("리뷰 목록 조회")
	class getReviews {

		Review review1, review2;
		ReviewContents reviewContents;
		MemberSchedule memberSchedule;

		@BeforeEach
		void init() {
			memberSchedule = MemberSchedule.builder()
				.member(MemberFixture.JWA_SO_YEON.toMemberWithId(1))
				.schedule(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule())
				.build();
			review1 = ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule);
			review2 = ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule);
			reviewContents = ReviewContentsFixture.REVIEW_CONTENTS.toReviewContents();

			given(reviewContentsRepository.findByReviewIdAndDeletedAtNull(anyInt()))
				.willReturn(reviewContents);
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
		Review review1, review2;
		ReviewContents reviewContents;
		MemberSchedule memberSchedule;

		@BeforeEach
		void init() {
			memberSchedule = MemberSchedule.builder()
				.member(MemberFixture.JWA_SO_YEON.toMemberWithId(1))
				.schedule(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule())
				.build();
			review1 = ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule);
			review2 = ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule);
			reviewContents = ReviewContentsFixture.REVIEW_CONTENTS.toReviewContents();

			given(memberScheduleService.getMemberScheduleByMemberAndSchedule(anyInt(), any(Schedule.class)))
				.willReturn(memberSchedule);
			given(scheduleRepository.findById(anyInt()))
				.willReturn(Optional.of(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule()));
			given(reviewContentsRepository.findByReviewIdAndDeletedAtNull(anyInt()))
				.willReturn(reviewContents);
		}

		@Test
		@DisplayName("최신순")
		void newest() {
			// Given
			given(reviewRepository.getReviewsByMemberScheduleOrderByCreatedAtDesc(any(MemberSchedule.class)))
				.willReturn(List.of(review1, review2));
			// When
			ReviewListResponse reviewListResponse = reviewService.getReviewsByScheduleId(1, 1, ReviewType.NEWEST);
			// Then
			assertThat(reviewListResponse.reviewCount()).isEqualTo(2);
		}

		@Test
		@DisplayName("오래된 순")
		void oldest() {
			// Given
			given(reviewRepository.getReviewsByMemberScheduleOrderByCreatedAt(any(MemberSchedule.class)))
				.willReturn(List.of(review1, review2));
			// When
			ReviewListResponse reviewListResponse = reviewService.getReviewsByScheduleId(1, 1, ReviewType.OLDEST);
			// Then
			assertThat(reviewListResponse.reviewCount()).isEqualTo(2);
		}
	}
}
