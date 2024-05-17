package com.butter.wypl.group.service;

import static com.butter.wypl.global.common.Color.*;
import static com.butter.wypl.group.fixture.GroupFixture.*;
import static com.butter.wypl.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.ServiceTest;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.group.data.request.GroupUpdateRequest;
import com.butter.wypl.group.domain.Group;
import com.butter.wypl.group.domain.GroupInviteState;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.exception.GroupErrorCode;
import com.butter.wypl.group.exception.GroupException;
import com.butter.wypl.group.repository.GroupRepository;
import com.butter.wypl.group.repository.MemberGroupRepository;
import com.butter.wypl.group.utils.GroupServiceUtils;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.repository.MemberRepository;

import jakarta.persistence.EntityManager;

@ServiceTest
class GroupModifyServiceJpaTest {

	@Autowired
	private GroupModifyService groupModifyService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private MemberGroupRepository memberGroupRepository;

	@Autowired
	private EntityManager em;

	@Nested
	@DisplayName("그룹 수정 테스트")
	class updateGroupTest {

		@Test
		@DisplayName("그룹 수정 성공")
		void whenSuccess() {

			/* Given */
			Member member = memberRepository.save(HAN_JI_WON.toMember());
			Group group = groupRepository.save(
				Group.of(GROUP_STUDY.getName(), GROUP_STUDY.getColor(), member));
			memberGroupRepository.save(MemberGroup.of(member, group, labelYellow, GroupInviteState.ACCEPTED));

			em.flush();
			em.clear();

			String modifyGroupName = "변경된 그룹명";
			Color modifyGroupColor = labelPink;

			GroupUpdateRequest updateRequest = new GroupUpdateRequest(modifyGroupName,
				modifyGroupColor);

			/* When */
			groupModifyService.updateGroup(member.getId(), group.getId(), updateRequest);
			Group updatedGroup = GroupServiceUtils.findById(groupRepository, group.getId());

			/* Then */
			assertEquals(modifyGroupName, updatedGroup.getName());
			assertEquals(modifyGroupColor, updatedGroup.getColor());

		}

		@Test
		@DisplayName("그룹 수정 실패 : 그룹 맴버의 요청이 아닌 경우")
		void whenFail() {

			/* Given */
			Member member = memberRepository.save(HAN_JI_WON.toMember());
			Member otherMember = memberRepository.save(KIM_JEONG_UK.toMember());
			Group group = groupRepository.save(
				Group.of(GROUP_STUDY.getName(), GROUP_STUDY.getColor(), member));

			em.flush();
			em.clear();

			String modifyGroupName = "변경된 그룹명";
			GroupUpdateRequest updateRequest = new GroupUpdateRequest(modifyGroupName, labelPink);

			/* When, Then */
			Assertions.assertThatThrownBy(() -> {
					groupModifyService.updateGroup(otherMember.getId(), group.getId(), updateRequest);
				}).isInstanceOf(GroupException.class)
				.hasMessageContaining(GroupErrorCode.IS_NOT_GROUP_MEMBER.getMessage());
		}
	}

	@Nested
	@DisplayName("그룹 탈퇴 테스트")
	class leaveGroupTest {

		private Member owner;
		private Member member1;
		private Group group;
		private MemberGroup memberGroup1;
		private MemberGroup memberGroup2;

		@BeforeEach
		void setUp() {
			owner = memberRepository.save(HAN_JI_WON.toMemberWithId(1));
			member1 = memberRepository.save(KIM_JEONG_UK.toMemberWithId(2));
			group = groupRepository.save(GROUP_STUDY.toGroup(owner));
			memberGroup1 = memberGroupRepository.save(MemberGroup.of(member1, group));
			memberGroup2 = memberGroupRepository.save(MemberGroup.of(owner, group));
			memberGroup2.setGroupInviteStateAccepted();
			memberGroup1.setGroupInviteStateAccepted();
			em.flush();
			em.clear();
		}

		@Test
		@DisplayName("그룹 탈퇴 성공")
		void whenSuccess() {
			System.out.println("================");
			assertThatCode(() -> {
				groupModifyService.leaveGroup(member1.getId(), group.getId());
			}).doesNotThrowAnyException();
			em.flush();
			em.clear();

		}
	}
}