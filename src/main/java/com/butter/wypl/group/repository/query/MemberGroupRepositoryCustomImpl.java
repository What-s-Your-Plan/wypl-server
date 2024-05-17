package com.butter.wypl.group.repository.query;

import static com.butter.wypl.group.domain.QGroup.*;
import static com.butter.wypl.group.domain.QMemberGroup.*;
import static com.butter.wypl.member.domain.QMember.*;

import java.util.List;
import java.util.Optional;

import com.butter.wypl.group.domain.GroupInviteState;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.member.domain.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberGroupRepositoryCustomImpl implements MemberGroupRepositoryCustom {

	private final JPAQueryFactory query;
	private final QMember groupOwner = new QMember("groupOwner");

	@Override
	public Optional<MemberGroup> findAcceptedWithGroupAndOwner(int memberId, int groupId) {

		MemberGroup findMemberGroup = query.selectFrom(memberGroup)
			.leftJoin(memberGroup.member, member).fetchJoin()
			.leftJoin(memberGroup.group, group).fetchJoin()
			.leftJoin(group.owner, groupOwner).fetchJoin()
			.where(group.id.eq(groupId)
				.and(member.id.eq(memberId))
				.and(memberGroup.groupInviteState.eq(GroupInviteState.ACCEPTED))
				.and(memberGroup.member.deletedAt.isNull()))
			.fetchFirst();
		return Optional.ofNullable(findMemberGroup);
	}

	@Override
	public Optional<MemberGroup> findPendingMemberGroup(int memberId, int groupId) {
		MemberGroup findMemberGroup = query.selectFrom(memberGroup)
			.join(memberGroup.member, member).fetchJoin()
			.join(memberGroup.group, group).fetchJoin()
			.where(group.id.eq(groupId)
				.and(member.id.eq(memberId))
				.and(memberGroup.groupInviteState.eq(GroupInviteState.PENDING))
				.and(member.deletedAt.isNull()))
			.fetchFirst();
		return Optional.ofNullable(findMemberGroup);
	}

	@Override
	public Optional<MemberGroup> findAcceptMemberGroup(int memberId, int groupId) {
		MemberGroup findMemberGroup = query.selectFrom(memberGroup)
			.join(memberGroup.member, member).fetchJoin()
			.join(memberGroup.group, group).fetchJoin()
			.where(group.id.eq(groupId)
				.and(member.id.eq(memberId))
				.and(memberGroup.groupInviteState.eq(GroupInviteState.ACCEPTED))
				.and(member.deletedAt.isNull()))
			.fetchFirst();
		return Optional.ofNullable(findMemberGroup);
	}

	@Override
	public List<MemberGroup> findAllAccepted(int groupId) {
		return query.selectFrom(memberGroup)
			.join(memberGroup.member, member).fetchJoin()
			.join(memberGroup.group, group).fetchJoin()
			.where(group.id.eq(groupId)
				.and(memberGroup.groupInviteState.eq(GroupInviteState.ACCEPTED))
				.and(member.deletedAt.isNull()))
			.fetch();
	}

	@Override
	public List<MemberGroup> findAll(int groupId) {
		return query.selectFrom(memberGroup)
			.join(memberGroup.member, member).fetchJoin()
			.join(memberGroup.group, group).fetchJoin()
			.where(group.id.eq(groupId)
				.and(member.deletedAt.isNull())
				.and(group.deletedAt.isNull()))
			.fetch();
	}

	@Override
	public List<MemberGroup> findAllWithMemberAndGroupByMemberId(final int memberId) {
		return query.selectFrom(memberGroup)
			.join(memberGroup.member, member).fetchJoin()
			.join(memberGroup.group, group).fetchJoin()
			.where(memberGroup.member.id.eq(memberId)
				.and(memberGroup.deletedAt.isNull())
				.and(member.deletedAt.isNull()))
			.fetch();
	}

	@Override
	public int getSizeOfGroupMembers(int groupId) {
		Long count = query.select(memberGroup.count())
			.from(memberGroup)
			.where(memberGroup.group.id.eq(groupId)
				.and(memberGroup.groupInviteState.eq(GroupInviteState.ACCEPTED))
				.and(memberGroup.member.deletedAt.isNull()))
			.fetchOne();
		assert count != null;
		return count.intValue();
	}

	@Override
	public void deleteByMemberIdAndGroupId(int memberId, int groupId) {
		query.delete(memberGroup)
			.where(memberGroup.member.id.eq(memberId)
				.and(memberGroup.group.id.eq(groupId)))
			.execute();
	}
}
