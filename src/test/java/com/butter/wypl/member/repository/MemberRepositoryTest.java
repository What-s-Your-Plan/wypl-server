package com.butter.wypl.member.repository;

import static com.butter.wypl.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.JpaRepositoryTest;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;

@JpaRepositoryTest
class MemberRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;

	@DisplayName("회원을 데이터베이스에 저장한다.")
	@ParameterizedTest
	@EnumSource(value = MemberFixture.class)
	void saveByMemberTest(MemberFixture memberFixture) {
		/* Given */
		Member member = memberFixture.toMember();

		/* When */
		/* Then */
		assertThatCode(() -> {
			memberRepository.save(member);
		}).doesNotThrowAnyException();
	}

	@DisplayName("회원의 닉네임을 변경한다.")
	@Test
	void updateNickname() {
		/* Given */
		Member member = KIM_JEONG_UK.toMember();
		member.changeNickname("wypl");

		/* When & Then */
		assertThatCode(() -> memberRepository.save(member))
				.doesNotThrowAnyException();
	}

	@DisplayName("회원의 생일을 변경한다.")
	@Test
	void updateBirthdayTest() {
		/* Given */
		Member member = KIM_JEONG_UK.toMember();
		member.changeBirthday(KIM_JEONG_UK.getBirthday());

		/* When & Then */
		assertThatCode(() -> memberRepository.save(member))
				.doesNotThrowAnyException();
	}
}