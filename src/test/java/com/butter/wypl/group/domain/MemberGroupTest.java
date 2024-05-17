package com.butter.wypl.group.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.JpaRepositoryTest;
import com.butter.wypl.group.fixture.GroupFixture;
import com.butter.wypl.group.repository.GroupRepository;
import com.butter.wypl.group.repository.MemberGroupRepository;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.MemberRepository;

import jakarta.persistence.EntityManager;

@JpaRepositoryTest
class MemberGroupTest {

	@Autowired
	private MemberGroupRepository memberGroupRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private EntityManager em;

	@Test
	@DisplayName("그룹 초대 상태를 ACCEPTED로 변경한다.")
	void setGroupInviteStateAcceptedTest() {

		/* Given */
		Member owner = memberRepository.save(MemberFixture.HAN_JI_WON.toMember());
		Member member = memberRepository.save(MemberFixture.KIM_JEONG_UK.toMember());
		Group group = groupRepository.save(GroupFixture.GROUP_STUDY.toGroup(owner));
		MemberGroup savedMemberGroup = memberGroupRepository.save(MemberGroup.of(member, group));
		assertThat(savedMemberGroup.getGroupInviteState()).isEqualTo(GroupInviteState.PENDING);

		/* When */
		savedMemberGroup.setGroupInviteStateAccepted();
		em.flush();
		em.clear();

		/* Then */
		assertThatCode(() -> {
			List<MemberGroup> memberGroups = memberGroupRepository.findAll();
			assertThat(memberGroups).hasSize(1);

			Optional<MemberGroup> foundMemberGroup = memberGroupRepository
				.findPendingMemberGroup(member.getId(), group.getId());
			assertThat(foundMemberGroup).isEmpty();
		}).doesNotThrowAnyException();

	}
}