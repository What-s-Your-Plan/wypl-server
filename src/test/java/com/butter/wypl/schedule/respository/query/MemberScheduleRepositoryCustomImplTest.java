package com.butter.wypl.schedule.respository.query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.JpaRepositoryTest;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.review.domain.Review;
import com.butter.wypl.schedule.domain.Category;
import com.butter.wypl.schedule.domain.Schedule;

import jakarta.persistence.EntityManager;

@JpaRepositoryTest
class MemberScheduleRepositoryCustomImplTest {

	@Autowired
	private EntityManager em;

	@Autowired
	private MemberRepository memberRepository;


	@Nested
	@DisplayName("회원의 오늘 종료되는 스케줄 중 리뷰가 없는 스케줄 조회 테스트")
	class findMemberSchedulesEndingTodayWithoutReviewTest {
		@Test
		@DisplayName("조회 성공")
		void whenSuccess() {
			em.clear();

		}

		@Test
		@DisplayName("조회 성공: 결과가 0개인 경우")
		void whenSuccessOfResultZero() {

		}
	}

}