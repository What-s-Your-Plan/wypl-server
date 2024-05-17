package com.butter.wypl.group.utils;

import java.util.List;

import com.butter.wypl.global.annotation.Generated;
import com.butter.wypl.global.exception.CallConstructorException;
import com.butter.wypl.group.domain.Group;
import com.butter.wypl.group.exception.GroupErrorCode;
import com.butter.wypl.group.exception.GroupException;
import com.butter.wypl.group.repository.GroupRepository;
import com.butter.wypl.member.domain.Member;

public class GroupServiceUtils {

	@Generated
	private GroupServiceUtils() {
		throw new CallConstructorException();
	}

	public static Group findById(final GroupRepository groupRepository, final int groupId) {
		return groupRepository.findById(groupId)
			.orElseThrow(() -> new GroupException(GroupErrorCode.NOT_EXIST_GROUP));
	}

	public static void isGroupMember(final int userId, final List<Member> groupMembers) {
		if (groupMembers.stream().noneMatch(member -> member.getId() == userId)) {
			throw new GroupException(GroupErrorCode.IS_NOT_GROUP_MEMBER);
		}
	}

	public static boolean isGroupOwner(Member member, Group group) {
		return group.getOwner().getId() == member.getId();
	}
}
