package com.butter.wypl.review.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.JpaRepositoryTest;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.review.domain.Review;
import com.butter.wypl.review.fixture.ReviewFixture;
import com.butter.wypl.schedule.domain.MemberSchedule;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.fixture.ScheduleFixture;
import com.butter.wypl.schedule.respository.MemberScheduleRepository;
import com.butter.wypl.schedule.respository.ScheduleRepository;

@JpaRepositoryTest
public class ReviewRepositoryTest {

	private final ReviewRepository reviewRepository;

	private final MemberScheduleRepository memberScheduleRepository;

	private final ScheduleRepository scheduleRepository;

	private final MemberRepository memberRepository;

	@Autowired
	public ReviewRepositoryTest(ReviewRepository reviewRepository, MemberScheduleRepository memberScheduleRepository,
			ScheduleRepository scheduleRepository, MemberRepository memberRepository) {
		this.reviewRepository = reviewRepository;
		this.memberScheduleRepository = memberScheduleRepository;
		this.scheduleRepository = scheduleRepository;
		this.memberRepository = memberRepository;
	}

	@Test
	@DisplayName("리뷰 저장")
	void createReview() {
		// Given
		Member member = memberRepository.save(MemberFixture.KIM_JEONG_UK.toMemberWithId(1));
		Schedule schedule = scheduleRepository.save(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule());

		MemberSchedule memberSchedule = memberScheduleRepository.save(
			MemberSchedule.of(1, member, schedule)
		);

		Review review = ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule);

		// When
		Review savedReview = reviewRepository.save(review);

		// Then
		assertThat(savedReview).isNotNull();
		assertThat(savedReview.getTitle()).isEqualTo(review.getTitle());
	}

	@Test
	@DisplayName("상세 리뷰 조회")
	void getDetailReview() {
		// Given
		Member member = memberRepository.save(MemberFixture.KIM_JEONG_UK.toMemberWithId(1));
		Schedule schedule = scheduleRepository.save(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule());

		MemberSchedule memberSchedule = memberScheduleRepository.save(
			MemberSchedule.of(1, member, schedule)
		);

		Review review = ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule);
		Review savedReview = reviewRepository.save(review);

		// When
		Review findReview = reviewRepository.getByReviewId(savedReview.getReviewId());

		// Then
		assertThat(findReview.getMemberSchedule()).isEqualTo(savedReview.getMemberSchedule());
		assertThat(findReview.getTitle()).isEqualTo(savedReview.getTitle());
	}

	@Nested
	@DisplayName("특정 일정에 해당하는 회고 목록 조회")
	class getReviewsBySchedule {

		@Test
		@DisplayName("오래된 순")
		void getOldest() {
			// Given
			Member member = memberRepository.save(MemberFixture.KIM_JEONG_UK.toMemberWithId(1));
			Schedule schedule = scheduleRepository.save(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule());

			MemberSchedule memberSchedule = memberScheduleRepository.save(
				MemberSchedule.of(1, member, schedule)
			);

			reviewRepository.save(
					ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule));
			reviewRepository.save(
					ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule));

			// When
			List<Review> reviews = reviewRepository.getReviewsByMemberScheduleOrderByCreatedAt(memberSchedule);

			// Then
			assertThat(reviews.size()).isEqualTo(2);
			assertThat(reviews.get(0).getCreatedAt()).isBefore(reviews.get(1).getCreatedAt());
		}

		@Test
		@DisplayName("최신순")
		void getNewest() {
			// Given
			Member member = memberRepository.save(MemberFixture.KIM_JEONG_UK.toMemberWithId(1));
			Schedule schedule = scheduleRepository.save(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule());

			MemberSchedule memberSchedule = memberScheduleRepository.save(
				MemberSchedule.of(1, member, schedule)
			);

			reviewRepository.save(
					ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule));
			reviewRepository.save(
					ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule));

			// When
			List<Review> reviews = reviewRepository.getReviewsByMemberScheduleOrderByCreatedAtDesc(memberSchedule);

			// Then
			assertThat(reviews.size()).isEqualTo(2);
			assertThat(reviews.get(0).getCreatedAt()).isAfter(reviews.get(1).getCreatedAt());

		}
	}

	@Nested
	@DisplayName("멤버의 회고 목록 조회")
	class getReviewsByMember {

		Member member;
		Schedule schedule;

		MemberSchedule memberSchedule;

		Review review1, review2;

		@BeforeEach
		void init() {
			member = memberRepository.save(MemberFixture.KIM_JEONG_UK.toMember());
			schedule = scheduleRepository.save(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule());

			memberSchedule = memberScheduleRepository.save(
				MemberSchedule.of(1, member, schedule)
			);

			review1 = reviewRepository.save(
					ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule));
			review2 = reviewRepository.save(
					ReviewFixture.STUDY_REVIEW_2.toReviewWithMemberSchedule(memberSchedule));
		}

		@Test
		@DisplayName("날짜 지정 x, 처음 조회, 오래된 순")
		void getReviews1() {
			// Given
			// When
			List<Review> reviews = reviewRepository.getReviewsOldestAll(member.getId(), 0);

			// Then
			assertThat(reviews.size()).isEqualTo(2);
			assertThat(reviews.get(0).getCreatedAt()).isBefore(reviews.get(1).getCreatedAt());
		}

		@Test
		@DisplayName("날짜 지정 x, 처음 이후 조회, 오래된 순")
		void getReviews2() {
			// Given
			// When
			List<Review> reviews = reviewRepository.getReviewsOldestAll(member.getId(), review1.getReviewId());

			// Then
			assertThat(reviews.size()).isEqualTo(1);
		}

		@Test
		@DisplayName("날짜 지정 x, 처음 조회, 최신 순")
		void getReviews3() {
			// Given

			// When
			List<Review> reviews = reviewRepository.getReviewsNewestAll(member.getId());

			// Then
			assertThat(reviews.size()).isEqualTo(2);
			assertThat(reviews.get(0).getCreatedAt()).isAfter(reviews.get(1).getCreatedAt());
		}

		@Test
		@DisplayName("날짜 지정 x, 처음 이후 조회, 최신 순")
		void getReviews4() {
			// Given

			// When
			List<Review> reviews = reviewRepository.getReviewsNewestAllAfter(member.getId(), review2.getReviewId());

			// Then
			assertThat(reviews.size()).isEqualTo(1);
		}

		@Test
		@DisplayName("날짜 지정 o, 처음 조회, 오래된 순")
		void getReviews5() {
			// Given

			// When
			List<Review> reviews = reviewRepository.getReviewsOldest(member.getId(), 0,
					LocalDateTime.now().minusDays(1),
					LocalDateTime.now().plusDays(1));

			// Then
			assertThat(reviews.size()).isEqualTo(2);
			assertThat(reviews.get(0).getReviewId()).isLessThan(reviews.get(1).getReviewId());
		}

		@Test
		@DisplayName("날짜 지정 o, 처음 이후 조회, 오래된 순")
		void getReviews6() {
			// Given

			// When
			List<Review> reviews = reviewRepository.getReviewsOldest(member.getId(), review1.getReviewId(),
					LocalDateTime.now().minusDays(1),
					LocalDateTime.now().plusDays(1));

			// Then
			assertThat(reviews.size()).isEqualTo(1);

		}

		@Test
		@DisplayName("날짜 지정 o, 처음 조회, 최신 순")
		void getReviews7() {
			// Given

			// When
			List<Review> reviews = reviewRepository.getReviewsNewest(member.getId(), LocalDateTime.now().minusDays(1),
					LocalDateTime.now().plusDays(1));

			// Then
			assertThat(reviews.size()).isEqualTo(2);
			assertThat(reviews.get(0).getCreatedAt()).isAfter(reviews.get(1).getCreatedAt());

		}

		@Test
		@DisplayName("날짜 지정 o, 처음 이후 조회, 최신 순")
		void getReviews8() {
			// Given

			// When
			List<Review> reviews = reviewRepository.getReviewsNewestAfter(member.getId(), review2.getReviewId(),
					LocalDateTime.now().minusDays(1),
					LocalDateTime.now().plusDays(1));

			// Then
			assertThat(reviews.size()).isEqualTo(1);
		}
	}
}
