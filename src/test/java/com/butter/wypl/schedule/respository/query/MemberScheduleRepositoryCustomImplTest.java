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
import com.butter.wypl.schedule.domain.MemberSchedule;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.respository.MemberScheduleRepository;

import jakarta.persistence.EntityManager;

@JpaRepositoryTest
class MemberScheduleRepositoryCustomImplTest {

	@Autowired
	private EntityManager em;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberScheduleRepository memberScheduleRepository;

	@Nested
	@DisplayName("회원의 오늘 종료되는 스케줄 중 리뷰가 없는 스케줄 조회 테스트")
	class findMemberSchedulesEndingTodayWithoutReviewTest {
		@Test
		@DisplayName("조회 성공")
		void whenSuccess() {
			em.clear();
			/* Given */
			Member member = memberRepository.save(MemberFixture.HAN_JI_WON.toMember());
			Schedule schedule = Schedule.builder()
				.title("테니스 스케쥴")
				.description(" 테니스 강습받기")
				.category(Category.MEMBER)
				.startDate(LocalDateTime.of(2024, 5, 15, 10, 0))
				.endDate(LocalDateTime.of(2024, 5, 15, 14, 30))
				.build();

			Schedule schedule2 = Schedule.builder()
				.title("페스티벌 가기")
				.description("2024 서울 페스티벌")
				.category(Category.MEMBER)
				.startDate(LocalDateTime.of(2024, 5, 14, 23, 0))
				.endDate(LocalDateTime.of(2024, 5, 15, 0, 0))
				.build();

			MemberSchedule memberSchedule = MemberSchedule.of(member, schedule);
			MemberSchedule memberSchedule2 = MemberSchedule.of(member, schedule2);
			Review review = Review.builder()
				.title(" This is 테니스 스케쥴 리뷰...신난다. ")
				.memberSchedule(memberSchedule)
				.build();

			em.persist(member);
			em.persist(schedule);
			em.persist(schedule2);
			em.persist(memberSchedule);
			em.persist(memberSchedule2);
			em.persist(review);
			em.flush();
			em.clear();
			int memberId = member.getId();
			LocalDate today = LocalDate.of(2024, 5, 15);

			/* When */
			List<MemberSchedule> memberSchedules = memberScheduleRepository.findMemberSchedulesEndingTodayWithoutReview(
				memberId, today);

			/* Then */
			Assertions.assertThat(memberSchedules).hasSize(1);

		}

		@Test
		@DisplayName("조회 성공: 결과가 0개인 경우")
		void whenSuccessOfResultZero() {
			em.clear();
			/* Given */
			Member member = memberRepository.save(MemberFixture.HAN_JI_WON.toMember());
			Schedule schedule = Schedule.builder()
				.title("테니스 스케쥴")
				.description(" 테니스 강습받기")
				.category(Category.MEMBER)
				.startDate(LocalDateTime.of(2024, 5, 15, 10, 0))
				.endDate(LocalDateTime.of(2024, 5, 15, 14, 30))
				.build();

			Schedule schedule2 = Schedule.builder()
				.title("페스티벌 가기")
				.description("2024 서울 페스티벌")
				.category(Category.MEMBER)
				.startDate(LocalDateTime.of(2024, 5, 14, 23, 0))
				.endDate(LocalDateTime.of(2024, 5, 15, 0, 0))
				.build();

			MemberSchedule memberSchedule = MemberSchedule.of(member, schedule);
			MemberSchedule memberSchedule2 = MemberSchedule.of(member, schedule2);
			Review review = Review.builder()
				.title(" This is 테니스 스케쥴 리뷰...신난다. ")
				.memberSchedule(memberSchedule)
				.build();
			Review review2 = Review.builder()
				.title(" This is 서울 페스티벌 스케쥴 리뷰.... ")
				.memberSchedule(memberSchedule2)
				.build();

			em.persist(member);
			em.persist(schedule);
			em.persist(schedule2);
			em.persist(memberSchedule);
			em.persist(memberSchedule2);
			em.persist(review);
			em.persist(review2);
			em.flush();
			em.clear();
			int memberId = member.getId();
			LocalDate today = LocalDate.of(2024, 5, 15);

			/* When */
			List<MemberSchedule> memberSchedules = memberScheduleRepository.findMemberSchedulesEndingTodayWithoutReview(
				memberId, today);

			/* Then */
			Assertions.assertThat(memberSchedules).isNotNull();
			Assertions.assertThat(memberSchedules).hasSize(0);

		}
	}

}