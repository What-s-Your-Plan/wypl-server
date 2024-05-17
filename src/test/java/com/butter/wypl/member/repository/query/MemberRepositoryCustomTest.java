package com.butter.wypl.member.repository.query;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.JpaRepositoryTest;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.member.repository.query.data.MemberSearchCond;

import jakarta.persistence.EntityManager;

@JpaRepositoryTest
class MemberRepositoryCustomTest {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private EntityManager em;

	@DisplayName("SearchMember Test")
	@ParameterizedTest
	@ValueSource(strings = {"wo", "03"})
	void searchMemberTest(String query) {
		/* Given */
		List<Member> members = Arrays.stream(MemberFixture.values())
				.map(MemberFixture::toMember)
				.toList();
		memberRepository.saveAll(members);

		int expected = getExpected(query, members);
		MemberSearchCond cond = new MemberSearchCond(query, 5);

		/* When */
		List<Member> findMembers = memberRepository.findBySearchCond(cond);

		/* Then */
		assertThat(findMembers).size().isEqualTo(expected);
	}

	private int getExpected(String query, List<Member> members) {
		int expected = 0;
		for (Member member : members) {
			if (member.getEmail().contains(query)) {
				expected++;
			}
		}
		return expected;
	}
}