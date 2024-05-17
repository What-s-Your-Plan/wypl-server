package com.butter.wypl.group.utils;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.global.annotation.Generated;
import com.butter.wypl.global.exception.CallConstructorException;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.exception.GroupErrorCode;
import com.butter.wypl.group.exception.GroupException;
import com.butter.wypl.group.repository.MemberGroupRepository;
import com.butter.wypl.member.domain.Member;

@Transactional(readOnly = true)
public class MemberGroupServiceUtils {

	@Generated
	private MemberGroupServiceUtils() {
		throw new CallConstructorException();
	}

	public static MemberGroup getAcceptMemberGroup(
		MemberGroupRepository memberGroupRepository,
		int memberId,
		int groupId
	) {
		return memberGroupRepository.findAcceptMemberGroup(memberId, groupId)
			.orElseThrow(() -> new GroupException(GroupErrorCode.NOT_EXIST_MEMBER_GROUP));
	}

	public static List<MemberGroup> getAcceptedMemberGroupsOfGroup(MemberGroupRepository memberGroupRepository,
		int groupId) {
		return memberGroupRepository.findAllAccepted(groupId);
	}

	public static List<Member> getAcceptedMembersOfGroup(MemberGroupRepository memberGroupRepository,
		int groupId) {
		return getAcceptedMemberGroupsOfGroup(memberGroupRepository, groupId)
			.stream()
			.map(MemberGroup::getMember)
			.toList();
	}

}
