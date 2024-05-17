package com.butter.wypl.group.service;

import static com.butter.wypl.group.exception.GroupErrorCode.*;
import static com.butter.wypl.group.fixture.GroupFixture.*;
import static com.butter.wypl.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.global.exception.CustomException;
import com.butter.wypl.group.data.request.GroupCreateRequest;
import com.butter.wypl.group.data.request.GroupMemberInviteRequest;
import com.butter.wypl.group.domain.Group;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.exception.GroupErrorCode;
import com.butter.wypl.group.exception.GroupException;
import com.butter.wypl.group.repository.GroupRepository;
import com.butter.wypl.group.repository.MemberGroupRepository;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.notification.service.GroupNotificationService;

@MockServiceTest
class GroupModifyServiceTest {

	@InjectMocks
	private GroupModifyServiceImpl groupModifyService;

	@Mock
	private GroupRepository groupRepository;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private MemberGroupRepository memberGroupRepository;

	@Mock
	private GroupNotificationService groupNotificationService;

	@Nested
	@DisplayName("그룹 생성 테스트")
	class createGroupTest {

		private final Member owner = HAN_JI_WON.toMemberWithId(1);
		private final Member member1 = KIM_JEONG_UK.toMemberWithId(2);
		private final Member member2 = LEE_JI_WON.toMemberWithId(3);

		@Test
		@DisplayName("그룹 생성 성공")
		void createGroupSuccess() {
			/* Given */
			Group newGroup = GROUP_STUDY.toGroup(owner);
			GroupCreateRequest givenGroupCreateRequest = new GroupCreateRequest(newGroup.getName(),
				newGroup.getColor(), Set.of(member1.getId(), member2.getId()));
			List<Member> members = List.of(owner, member1, member2);

			given(memberRepository.findAllById(anySet()))
				.willReturn(members);

			given(groupRepository.save(any(Group.class)))
				.willReturn(newGroup);

			given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(owner));

			/* When, Then */
			assertThatCode(() -> {
				groupModifyService.createGroup(owner.getId(), givenGroupCreateRequest);
			}).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("그룹 생성 실패: 최대 인원 초과")
		void createGroupFailOfExceedMaxNumberCount() {
			/* Given */
			HashSet<Integer> memberIdList = new HashSet<>();
			for (int i = 1; i <= 51; i++) {
				memberIdList.add(i);
			}
			GroupCreateRequest givenGroupCreateRequest =
				new GroupCreateRequest("name", Color.labelBrown, memberIdList);

			given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(owner));

			/* When, Then */
			Assertions.assertThatThrownBy(() -> {
					groupModifyService.createGroup(owner.getId(), givenGroupCreateRequest);
				}).isInstanceOf(GroupException.class)
				.hasMessageContaining(GroupErrorCode.EXCEED_MAX_MEMBER_COUNT.getMessage());
		}

		@Test
		@DisplayName("그룹 생성 실패: 유효하지 않은 회원 ID 존재")
		void createGroupFailOfExistsNotInvalidMemberId() {
			/* Given */
			int givenMemberId = 1;
			GroupCreateRequest givenGroupCreateRequest = new GroupCreateRequest("name", Color.labelBrown,
				new HashSet<>(Arrays.asList(2, 3)));

			given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(owner));

			/* When, Then */
			Assertions.assertThatThrownBy(() -> {
					groupModifyService.createGroup(givenMemberId, givenGroupCreateRequest);
				}).isInstanceOf(GroupException.class)
				.hasMessageContaining(EXISTS_INVALID_MEMBER.getMessage());
		}

		@Test
		@DisplayName("그룹 생성 실패: 그룹 인원 중 그룹 갯수 초과 회원 존재")
		void createGroupFailOfExistsMemberExceedingGroupCountLimit() {
			/* Given */
			Member member3 = MemberFixture.JWA_SO_YEON.toMemberWithId(4);
			for (int i = 1; i <= 50; i++) {
				member3.getMemberGroups().add(mock(MemberGroup.class));
			}
			List<Member> members = List.of(owner, member1, member2, member3);

			Set<Integer> memberIdList = Set.of(member1.getId(), member2.getId(), member3.getId());
			GroupCreateRequest givenGroupCreateRequest =
				new GroupCreateRequest("name", Color.labelBrown, memberIdList);

			given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(owner));

			given(memberRepository.findAllById(anySet()))
				.willReturn(members);

			Group savedGroup = GROUP_STUDY.toGroup(owner);
			given(groupRepository.save(any(Group.class))).willReturn(savedGroup);

			/* When, Then */
			Assertions.assertThatThrownBy(() -> {
					groupModifyService.createGroup(owner.getId(), givenGroupCreateRequest);
				}).isInstanceOf(CustomException.class)
				.hasMessageContaining("해당 맴버는 인당 최대 50개의 그룹 생성을 초과했습니다.");
		}
	}

	@Nested
	@DisplayName("그룹 삭제 테스트")
	class deleteGroupTest {

		@Test
		@DisplayName("그룹 삭제 성공")
		void whenSuccess() {
			/* Given */
			int memberId = 1;
			int groupId = 1;

			Member member = HAN_JI_WON.toMemberWithId(memberId);
			Group group = GROUP_STUDY.toGroup(HAN_JI_WON.toMemberWithId(memberId));
			given(groupRepository.findById(anyInt()))
				.willReturn(Optional.of(group));

			given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(member));

			MemberGroup memberGroup = MemberGroup.of(member, group, Color.labelBrown);
			List<MemberGroup> memberGroups = List.of(memberGroup);
			given(memberGroupRepository.findAllAccepted(groupId))
				.willReturn(memberGroups);

			/* When */
			groupModifyService.deleteGroup(memberId, groupId);

			/* Then */
			assertAll(
				() -> assertThat(group.isDeleted()).isTrue(),
				() -> memberGroups.forEach(mg -> assertThat(mg.isDeleted()).isTrue())
			);
		}
	}

	@Nested
	@DisplayName("그룹 초대 테스트")
	class inviteGroupMember {

		private final Member owner = HAN_JI_WON.toMemberWithId(1);
		private final Member member1 = KIM_JEONG_UK.toMemberWithId(2);
		private final Member member2 = LEE_JI_WON.toMemberWithId(3);

		private final Group group = GROUP_STUDY.toGroup(owner);

		@Test
		@DisplayName("그룹 멤버 초대 성공")
		void whenSuccess() {

			/* Given */
			GroupMemberInviteRequest inviteRequest = new GroupMemberInviteRequest(
				Set.of(member1.getId(), member2.getId()));

			given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(owner));

			given(groupRepository.findById(anyInt()))
				.willReturn(Optional.of(group));

			List<Member> memberIdList = List.of(member1, member2);
			given(memberRepository.findAllById(anySet())).willReturn(memberIdList);

			groupNotificationService.createGroupNotification(anyInt(), anyString(), anyString(), anyInt());

			/* When, Then */
			assertThatCode(() -> {
				groupModifyService.inviteGroupMember(owner.getId(), group.getId(), inviteRequest);
			}).doesNotThrowAnyException();

		}

		@Test
		@DisplayName("그룹 멤버 초대 실패: 그룹 소유자가 아닌 경우")
		void whenFailOfHasNotInvitePermission() {
			/* Given */
			GroupMemberInviteRequest inviteRequest = new GroupMemberInviteRequest(
				Set.of(member2.getId()));

			given(memberRepository.findById(member1.getId()))
				.willReturn(Optional.of(member1));

			given(groupRepository.findById(anyInt()))
				.willReturn(Optional.of(group));

			/* When, Then */
			assertThatThrownBy(() -> {
				groupModifyService.inviteGroupMember(member1.getId(), group.getId(), inviteRequest);
			}).isInstanceOf(GroupException.class)
				.hasMessageContaining(HAS_NOT_INVITE_PERMISSION.getMessage());
		}
	}

	@Nested
	@DisplayName("그룹 탈퇴 테스트")
	class leaveGroupTest {

		private final Member owner = HAN_JI_WON.toMemberWithId(1);
		private final Member member1 = KIM_JEONG_UK.toMemberWithId(2);
		private final Group group = GROUP_STUDY.toGroup(owner);
		private final MemberGroup memberGroup1 = MemberGroup.of(member1, group);
		private final MemberGroup memberGroup2 = MemberGroup.of(owner, group);

		@Test
		@DisplayName("그룹 탈퇴 성공")
		void whenSuccess() {
			/* Given */
			MemberGroup memberGroup = MemberGroup.of(member1, group);
			memberGroup.setGroupInviteStateAccepted();

			given(memberGroupRepository.findAcceptedWithGroupAndOwner(anyInt(), anyInt()))
				.willReturn(Optional.of(memberGroup));

			/* When, Then */
			assertThatCode(() -> {
				groupModifyService.leaveGroup(member1.getId(), group.getId());
			}).doesNotThrowAnyException();

		}
	}

	@Nested
	@DisplayName("그룹 회원 초대 수락 테스트")
	class acceptGroupInvitationTest {

		private final Member owner = HAN_JI_WON.toMemberWithId(1);
		private final Member member1 = KIM_JEONG_UK.toMemberWithId(2);

		private final Group group = GROUP_STUDY.toGroup(owner);

		@Test
		@DisplayName("그룹 회원 초대 수락 성공")
		void whenSuccess() {
			/* Given */
			given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(member1));

			given(groupRepository.findById(anyInt()))
				.willReturn(Optional.of(group));

			MemberGroup memberGroup = MemberGroup.of(member1, group);
			given(memberGroupRepository.findPendingMemberGroup(anyInt(), anyInt()))
				.willReturn(Optional.of(memberGroup));

			/* When, Then */
			assertThatCode(() -> {
				groupModifyService.acceptGroupInvitation(member1.getId(), group.getId());
			}).doesNotThrowAnyException();
		}
	}
}