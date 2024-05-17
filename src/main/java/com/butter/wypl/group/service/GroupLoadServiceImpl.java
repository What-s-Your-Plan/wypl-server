package com.butter.wypl.group.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.group.data.response.FindGroupMembersResponse;
import com.butter.wypl.group.data.response.FindGroupsResponse;
import com.butter.wypl.group.domain.GroupInviteState;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.exception.GroupErrorCode;
import com.butter.wypl.group.exception.GroupException;
import com.butter.wypl.group.repository.MemberGroupRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class GroupLoadServiceImpl implements GroupLoadService {

	private final MemberGroupRepository memberGroupRepository;

	/**
	 * 해당 사용자의 그룹 목록을 조회합니다.
	 *
	 * @param memberId 조회 요청한 회원의 식별자
	 */
	@Override
	public FindGroupsResponse getGroupsByMemberId(int memberId) {
		List<MemberGroup> findMemberGroups = memberGroupRepository.findAllWithMemberAndGroupByMemberId(memberId);
		List<FindGroupsResponse.FindGroup> groups = new ArrayList<>();
		List<FindGroupsResponse.FindGroup> invitedGroups = new ArrayList<>();
		findMemberGroups.forEach(mg -> {
			if (mg.getGroupInviteState().equals(GroupInviteState.ACCEPTED)) {
				groups.add(FindGroupsResponse.FindGroup.from(mg));
			} else if (mg.getGroupInviteState().equals(GroupInviteState.PENDING)) {
				invitedGroups.add(FindGroupsResponse.FindGroup.from(mg));
			}
		});
		return FindGroupsResponse.of(groups, invitedGroups);
	}

	/**
	 * 그룹의 상세 정보를 조회합니다.
	 *
	 * @hidden `Fetch Join`으로 `MemberGroup -> Member`조회하지 않을 시 <p>`JPA N + 1`문제가 발생할 수 있습니다.
	 *
	 * @param memberId 조회 요청한 회원의 식별자
	 * @param groupId 조회 대상인 그룹의 식별자
	 *
	 */
	@Override
	public FindGroupMembersResponse getDetailById(
		final int memberId,
		final int groupId
	) {
		MemberGroup findMemberGroup = memberGroupRepository.findAcceptMemberGroup(memberId, groupId)
			.orElseThrow(() -> new GroupException(GroupErrorCode.NOT_EXIST_MEMBER_GROUP));

		List<MemberGroup> findMemberGroups = memberGroupRepository.findAll(groupId);
		List<FindGroupMembersResponse.FindGroupMember> list = findMemberGroups.stream()
			.map(FindGroupMembersResponse.FindGroupMember::from)
			.toList();

		return FindGroupMembersResponse.of(list, findMemberGroup.getColor());
	}
}
