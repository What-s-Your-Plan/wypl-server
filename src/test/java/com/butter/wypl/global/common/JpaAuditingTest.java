package com.butter.wypl.global.common;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.JpaRepositoryTest;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.MemberRepository;

@JpaRepositoryTest
public class JpaAuditingTest {

	@Autowired
	private MemberRepository memberRepository;

	@DisplayName("저장한 엔티티의 생성 시간은 존재한다.")
	@ParameterizedTest
	@EnumSource(value = MemberFixture.class)
	void existedByMemberCreatedAt(MemberFixture memberFixture) {
		/* Given */
		Member member = memberFixture.toMember();

		/* When */
		Member savedMember = memberRepository.save(member);

		/* Then */
		assertAll(
				() -> assertThat(savedMember.getCreatedAt()).isNotNull(),
				() -> assertThat(savedMember.getModifiedAt()).isNotNull()
		);
	}
}