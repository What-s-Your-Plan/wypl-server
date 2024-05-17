package com.butter.wypl.group.repository.query;

import java.util.List;
import java.util.Optional;

import com.butter.wypl.group.domain.MemberGroup;

public interface MemberGroupRepositoryCustom {

	Optional<MemberGroup> findAcceptedWithGroupAndOwner(int memberId, int groupId);

	Optional<MemberGroup> findPendingMemberGroup(int memberId, int groupId);

	Optional<MemberGroup> findAcceptMemberGroup(int memberId, int groupId);

	List<MemberGroup> findAll(int groupId);

	List<MemberGroup> findAllAccepted(int groupId);
	
	List<MemberGroup> findAllWithMemberAndGroupByMemberId(int memberId);

	int getSizeOfGroupMembers(int groupId);

	void deleteByMemberIdAndGroupId(int memberId, int groupId);

}
