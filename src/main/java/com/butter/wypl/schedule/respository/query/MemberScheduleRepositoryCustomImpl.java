package com.butter.wypl.schedule.respository.query;

import static com.butter.wypl.schedule.domain.QMemberSchedule.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.butter.wypl.schedule.domain.MemberSchedule;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberScheduleRepositoryCustomImpl implements MemberScheduleRepositoryCustom {

	private final JPAQueryFactory query;

	/**
	 * @param memberId
	 * @param today
	 * @return List<MemberSchedule>
	 * @implSpec 회원의 오늘 종료되는 스케줄 중 리뷰가 없는 스케줄을 조회,
	 * 			만약, 일치하는 스케쥴이 없다면 빈 리스트를 반환
	 * @hidden fetch join 회원, 스케줄, 리뷰
	 */
	@Override
	public List<MemberSchedule> findMemberSchedulesEndingTodayWithoutReview(int memberId, LocalDate today) {
		return query.selectFrom(memberSchedule)
			.innerJoin(memberSchedule.member).fetchJoin()
			.innerJoin(memberSchedule.schedule).fetchJoin()
			.leftJoin(memberSchedule.reviews).fetchJoin()
			.where(getEqualMemberId(memberId)
				.and(isEndedToday(today))
				.and(isEmptyReview()))
			.fetch();
	}

	private static Predicate isEmptyReview() {
		return memberSchedule.reviews.isEmpty();
	}

	private static BooleanExpression isEndedToday(LocalDate today) {
		LocalDateTime todayStart = today.atStartOfDay(); // 00:00:00
		LocalDateTime todayEnd = today.atTime(23, 59, 59); // 23:59:59
		return memberSchedule.schedule.endDate.between(todayStart, todayEnd);
	}

	private static BooleanExpression getEqualMemberId(int memberId) {
		return memberSchedule.member.id.eq(memberId);
	}
}
