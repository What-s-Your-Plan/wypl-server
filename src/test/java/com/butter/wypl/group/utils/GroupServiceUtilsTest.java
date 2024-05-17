package com.butter.wypl.group.utils;

import static com.butter.wypl.global.common.Color.*;
import static com.butter.wypl.group.fixture.GroupFixture.*;
import static com.butter.wypl.member.fixture.MemberFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.JpaRepositoryTest;
import com.butter.wypl.group.domain.Group;
import com.butter.wypl.group.domain.GroupInviteState;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.repository.GroupRepository;
import com.butter.wypl.group.repository.MemberGroupRepository;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.repository.MemberRepository;

@JpaRepositoryTest
class GroupServiceUtilsTest {

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberGroupRepository memberGroupRepository;

	@Nested
	@DisplayName("그룹 맴버 여부 확인 테스트")
	class isGroupMemberTest {

		@Test
		@DisplayName("성공 : 그룹 맴버인 경우")
		void whenSuccess() {
			/* Given */
			Member savedMember = memberRepository.save(HAN_JI_WON.toMember());
			Group savedGroup = groupRepository.save(GROUP_STUDY.toGroup(savedMember));
			memberGroupRepository.save(MemberGroup.of(savedMember, savedGroup, labelYellow, GroupInviteState.ACCEPTED));
			List<Member> memberList = MemberGroupServiceUtils.getAcceptedMembersOfGroup(memberGroupRepository,
				savedGroup.getId());

			/* When, Then */
			assertThatCode(() -> GroupServiceUtils.isGroupMember(savedMember.getId(), memberList))
				.doesNotThrowAnyException();

		}

	}

	@Nested
	@DisplayName("그룹 소유자 여부 확인 테스트")
	class isGroupOwnerTest {

		@Test
		@DisplayName("성공 : 그룹 소유자인 경우")
		void whenSuccess() {
			/* Given */
			Member member = HAN_JI_WON.toMemberWithId(1);
			Group group = GROUP_STUDY.toGroup(member);

			/* When, Then */
			assertThatCode(
				() -> GroupServiceUtils.isGroupOwner(member, group))
				.doesNotThrowAnyException();
		}

		@Test
		@DisplayName("실패 : 그룹 소유자가 아닌 경우")
		void whenFailOfNotOwner() {
			/* Given */
			Member member = HAN_JI_WON.toMemberWithId(1);
			Group group = GROUP_STUDY.toGroup(member);
			Member otherMember = KIM_JEONG_UK.toMemberWithId(2);

			/* When, Then */
			assertFalse(GroupServiceUtils.isGroupOwner(otherMember, group));
		}

	}

}