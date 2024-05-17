package com.butter.wypl.group.service;

import static com.butter.wypl.group.exception.GroupErrorCode.*;
import static com.butter.wypl.group.utils.GroupServiceUtils.findById;
import static com.butter.wypl.group.utils.GroupServiceUtils.*;
import static com.butter.wypl.group.utils.MemberGroupServiceUtils.*;
import static com.butter.wypl.member.utils.MemberServiceUtils.findById;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.global.exception.CustomErrorCode;
import com.butter.wypl.global.exception.CustomException;
import com.butter.wypl.group.data.request.GroupCreateRequest;
import com.butter.wypl.group.data.request.GroupMemberColorUpdateRequest;
import com.butter.wypl.group.data.request.GroupMemberInviteRequest;
import com.butter.wypl.group.data.request.GroupUpdateRequest;
import com.butter.wypl.group.data.request.MemberIdRequest;
import com.butter.wypl.group.data.response.GroupIdResponse;
import com.butter.wypl.group.data.response.GroupMemberColorUpdateResponse;
import com.butter.wypl.group.data.response.GroupResponse;
import com.butter.wypl.group.data.response.MemberIdResponse;
import com.butter.wypl.group.domain.Group;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.exception.GroupErrorCode;
import com.butter.wypl.group.exception.GroupException;
import com.butter.wypl.group.repository.GroupRepository;
import com.butter.wypl.group.repository.MemberGroupRepository;
import com.butter.wypl.group.utils.GroupServiceUtils;
import com.butter.wypl.group.utils.MemberGroupServiceUtils;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.notification.service.GroupNotificationService;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class GroupModifyServiceImpl implements GroupModifyService {

	private final GroupRepository groupRepository;
	private final MemberRepository memberRepository;
	private final MemberGroupRepository memberGroupRepository;
	private final GroupNotificationService groupNotificationService;

	@Override
	public GroupResponse createGroup(int ownerId, GroupCreateRequest createRequest) {
		Member findOwnerMember = findById(memberRepository, ownerId);
		Set<Integer> memberIds = new HashSet<>(createRequest.memberIdList());
		memberIds.add(findOwnerMember.getId());
		validateMaxMemberCount(memberIds);

		List<Member> members = memberRepository.findAllById(memberIds);
		validateAllMembersExist(members, memberIds);

		Group savedGroup = groupRepository.save(
			Group.of(createRequest.name(), createRequest.color(), getMember(ownerId)));
		saveAllMemberGroup(members, savedGroup);

		members.forEach(member -> {
			/* 그룹 초대 알림 전송 */
			if (member.getId() == ownerId)
				return;
			groupNotificationService.createGroupNotification(
				member.getId(), member.getNickname(), savedGroup.getName(), savedGroup.getId()
			);
		});

		return new GroupResponse(savedGroup.getId(), savedGroup.getName(), savedGroup.getColor());
	}

	@Override
	public GroupResponse updateGroup(int memberId, int groupId, GroupUpdateRequest updateRequest) {

		Member foundMember = getMember(memberId);
		Group foundGroup = getGroup(groupId);
		isGroupMember(foundMember.getId(), getAcceptedMembersOfGroup(memberGroupRepository, foundGroup.getId()));

		foundGroup.updateGroupInfo(updateRequest.name(), updateRequest.color());
		return new GroupResponse(foundGroup.getId(), foundGroup.getName(), foundGroup.getColor());
	}

	@Override
	public void deleteGroup(int memberId, int groupId) {

		Group foundGroup = getGroup(groupId);
		Member foundMember = getMember(memberId);
		validateOwnerPermission(foundMember, foundGroup, HAS_NOT_DELETE_PERMISSION);

		List<MemberGroup> findMemberGroups = getAcceptedMemberGroupsOfGroup(memberGroupRepository, groupId);
		findMemberGroups.forEach(BaseEntity::delete);
		foundGroup.delete();
	}

	@Override
	public GroupIdResponse inviteGroupMember(int ownerId, int groupId, GroupMemberInviteRequest inviteRequest) {

		Member owner = getMember(ownerId);
		Group group = getGroup(groupId);
		Set<Integer> memberIds = inviteRequest.memberIdList();

		validateOwnerPermission(owner, group, HAS_NOT_INVITE_PERMISSION);
		validateMaxMemberCount(memberIds);

		List<Member> members = memberRepository.findAllById(memberIds);
		validateAllMembersExist(members, memberIds);

		saveAllMemberGroup(members, group);
		members.forEach(member -> {
			/* 그룹 초대 알림 전송 */
			groupNotificationService.createGroupNotification(
				member.getId(), owner.getNickname(), group.getName(), group.getId()
			);
		});

		return new GroupIdResponse(group.getId());
	}

	@Override
	public MemberIdResponse forceOutGroupMember(int ownerId, int groupId, MemberIdRequest userIdRequest) {
		Member owner = getMember(ownerId);
		Group group = getGroup(groupId);
		validateOwnerPermission(owner, group, HAS_NOT_FORCE_OUT_PERMISSION);

		Member member = getMember(userIdRequest.memberId());
		memberGroupRepository.deleteByMemberIdAndGroupId(member.getId(), group.getId());
		return new MemberIdResponse(member.getId());
	}

	@Override
	public void acceptGroupInvitation(int memberId, int groupId) {
		MemberGroup memberGroup = getPendingMemberGroup(getMember(memberId), getGroup(groupId));
		memberGroup.setGroupInviteStateAccepted();
	}

	@Override
	public void rejectGroupInvitation(int memberId, int groupId) {
		MemberGroup memberGroup = getPendingMemberGroup(getMember(memberId), getGroup(groupId));
		memberGroupRepository.delete(memberGroup);
	}

	@Override
	public void leaveGroup(int memberId, int groupId) {
		MemberGroup memberGroup = findAndValidMemberGroup(memberId, groupId);

		if (isGroupOwner(memberId, memberGroup.getGroup().getOwner())
			&& hasMultipleGroupMembers(groupId)) {
			throw new GroupException(NOT_ACCEPTED_LEAVE_GROUP);
		}
		memberGroupRepository.delete(memberGroup);
	}

	@Override
	public GroupMemberColorUpdateResponse updateGroupColor(int memberId, int groupId,
		GroupMemberColorUpdateRequest request) {
		Color color = request.color();
		MemberGroup foundMemberGroup = getMemberGroup(memberId, groupId);
		foundMemberGroup.updateColor(color);
		return new GroupMemberColorUpdateResponse(color);
	}

	private static void validateOwnerPermission(Member member, Group group, GroupErrorCode errorCode) {
		if (!isGroupOwner(member.getId(), group.getOwner())) {
			throw new GroupException(errorCode);
		}
	}

	private MemberGroup findAndValidMemberGroup(int memberId, int groupId) {
		return memberGroupRepository.findAcceptedWithGroupAndOwner(memberId, groupId)
			.orElseThrow(() -> new GroupException(NOT_EXIST_MEMBER_GROUP));
	}

	private boolean hasMultipleGroupMembers(int groupId) {
		return memberGroupRepository.getSizeOfGroupMembers(groupId) > 1;
	}

	private static boolean isGroupOwner(int memberId, Member owner) {
		return owner.getId() == memberId;
	}

	private MemberGroup getMemberGroup(int memberId, int groupId) {
		return MemberGroupServiceUtils.getAcceptMemberGroup(memberGroupRepository, memberId, groupId);
	}

	private void saveAllMemberGroup(List<Member> members, Group group) {
		List<MemberGroup> memberGroups = new ArrayList<>();
		members.forEach(member -> {
				if (member.getMemberGroups().size() >= 50) {
					throw new CustomException(new CustomErrorCode(HttpStatus.BAD_REQUEST, "GROUP_CUSTOM",
						member.getEmail() + "해당 맴버는 인당 최대 50개의 그룹 생성을 초과했습니다."));
				}
				MemberGroup memberGroup = MemberGroup.of(member, group, group.getColor());
				if (GroupServiceUtils.isGroupOwner(member, group))
					memberGroup.setGroupInviteStateAccepted();
				memberGroups.add(memberGroup);
			}
		);
		memberGroupRepository.saveAll(memberGroups);
	}

	private void validateAllMembersExist(Collection<Member> members, Collection<Integer> memberIdList) {
		if (members.size() != memberIdList.size()) {
			throw new GroupException(EXISTS_INVALID_MEMBER);
		}
	}

	private void validateMaxMemberCount(Collection<?> members) {
		if (members.size() > 50) {
			throw new GroupException(EXCEED_MAX_MEMBER_COUNT);
		}
	}

	private MemberGroup getPendingMemberGroup(Member foundMember, Group foundGroup) {
		return memberGroupRepository.findPendingMemberGroup(foundMember.getId(),
				foundGroup.getId())
			.orElseThrow(() -> new GroupException(NOT_EXIST_PENDING_MEMBER_GROUP));
	}

	private Member getMember(int ownerId) {
		return findById(memberRepository, ownerId);
	}

	private Group getGroup(int groupId) {
		return findById(groupRepository, groupId);
	}
}
