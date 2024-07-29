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
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.fixture.ScheduleFixture;
import com.butter.wypl.schedule.respository.ScheduleRepository;

@JpaRepositoryTest
public class ReviewRepositoryTest {

	private final ReviewRepository reviewRepository;

	private final ScheduleRepository scheduleRepository;

	private final MemberRepository memberRepository;

	@Autowired
	public ReviewRepositoryTest(ReviewRepository reviewRepository,
			ScheduleRepository scheduleRepository, MemberRepository memberRepository) {
		this.reviewRepository = reviewRepository;
		this.scheduleRepository = scheduleRepository;
		this.memberRepository = memberRepository;
	}

	@Test
	@DisplayName("리뷰 저장")
	void createReview() {

	}

	@Test
	@DisplayName("상세 리뷰 조회")
	void getDetailReview() {

	}

	@Nested
	@DisplayName("특정 일정에 해당하는 회고 목록 조회")
	class getReviewsBySchedule {

		@Test
		@DisplayName("오래된 순")
		void getOldest() {

		}

		@Test
		@DisplayName("최신순")
		void getNewest() {

		}
	}

	@Nested
	@DisplayName("멤버의 회고 목록 조회")
	class getReviewsByMember {

	}
}
