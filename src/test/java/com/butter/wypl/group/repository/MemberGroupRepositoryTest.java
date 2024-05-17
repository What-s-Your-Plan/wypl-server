package com.butter.wypl.group.repository;

import static com.butter.wypl.global.common.Color.*;
import static com.butter.wypl.group.fixture.GroupFixture.*;
import static com.butter.wypl.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.JpaRepositoryTest;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.group.domain.Group;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.fixture.GroupFixture;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.MemberRepository;

import jakarta.persistence.EntityManager;

@JpaRepositoryTest
class MemberGroupRepositoryTest {

	@Autowired
	private MemberGroupRepository memberGroupRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private EntityManager em;

	@Test
	void countByMemberId() {
	}

	@Nested
	@DisplayName("회원 그룹 생성 테스트")
	class saveTest {

		@Test
		@DisplayName("회원 그룹 저장 성공")
		void whenSuccess() {

			/* Given */
			Member owner = memberRepository.save(MemberFixture.HAN_JI_WON.toMember());
			Group group = groupRepository.save(GroupFixture.GROUP_STUDY.toGroup(owner));
			MemberGroup memberGroup = MemberGroup.of(owner, group, Color.labelBlue);

			/* When */
			assertThatCode(() -> {
				memberGroupRepository.save(memberGroup);
			}).doesNotThrowAnyException();

			/* Then */
			em.flush();
			em.clear();

			assertThatCode(() -> {
				MemberGroup findMemberGroup = memberGroupRepository.findPendingMemberGroup(owner.getId(),
					group.getId()).orElseThrow();
				assertThat(findMemberGroup.getGroup().getName()).isNotNull().isEqualTo(group.getName());
				assertThat(findMemberGroup.getMember().getEmail()).isNotNull().isEqualTo(owner.getEmail());
			}).doesNotThrowAnyException();
		}
	}

	@Test
	@DisplayName("그룹아이디로 회원그룹 리스트 조회 성공")
	void findAllMemberGroupsByGroupId() {

		/* Given */
		Member savedMember = memberRepository.save(HAN_JI_WON.toMember());
		Member savedMember2 = memberRepository.save(LEE_JI_WON.toMember());
		Member savedMember3 = memberRepository.save(JO_DA_MIN.toMember());
		Group savedGroup = groupRepository.save(GROUP_STUDY.toGroup(savedMember));

		memberGroupRepository.save(MemberGroup.of(savedMember, savedGroup, labelRed));
		memberGroupRepository.save(MemberGroup.of(savedMember2, savedGroup, labelRed));
		memberGroupRepository.save(MemberGroup.of(savedMember3, savedGroup, labelRed));

		/* When */
		List<MemberGroup> foundMemberGroups = memberGroupRepository.findAll(savedGroup.getId());

		/* Then */
		assertThatCode(() -> {
			assertThat(foundMemberGroups)
				.isNotNull();
		}).doesNotThrowAnyException();

	}
}