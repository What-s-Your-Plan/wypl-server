package com.butter.wypl.member.repository.query;

import static com.butter.wypl.member.domain.QMember.*;

import java.util.List;

import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.repository.query.data.MemberSearchCond;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
	private final JPAQueryFactory query;

	@Override
	public List<Member> findBySearchCond(MemberSearchCond cond) {
		return query.selectFrom(member)
				.where((member.deletedAt.isNull())
						.and(member.email.like("%" + cond.q() + "%"))
				)
				.limit(cond.size())
				.fetch();
	}

	@Override
	public List<Member> findAllActiveMembers() {
		return query.selectFrom(member)
			.where(member.deletedAt.isNull())
			.fetch();
	}
}
