package com.butter.wypl.member.service;

import static com.butter.wypl.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.member.data.response.FindMemberProfileInfoResponse;
import com.butter.wypl.member.data.response.FindTimezonesResponse;
import com.butter.wypl.member.data.response.MemberColorsResponse;
import com.butter.wypl.member.data.response.MemberSearchResponse;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.exception.MemberErrorCode;
import com.butter.wypl.member.exception.MemberException;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.member.repository.query.data.MemberSearchCond;

@MockServiceTest
class MemberLoadServiceTest {
	@InjectMocks
	private MemberServiceImpl memberService;
	@Mock
	private MemberRepository memberRepository;

	private AuthMember authMember;

	@BeforeEach
	void setUp() {
		authMember = AuthMember.from(0);
	}

	@DisplayName("서버의 모든 타임존을 조회한다.")
	@Test
	void findAllTimezonesTest() {
		/* Given */
		Member member = KIM_JEONG_UK.toMember();
		given(memberRepository.findById(any(Integer.class)))
				.willReturn(Optional.of(member));

		/* When */
		FindTimezonesResponse response = memberService.findAllTimezones(authMember);

		/* Then */
		assertAll(
				() -> assertThat(response.memberTimeZone()).isNotNull(),
				() -> assertThat(response.timezones()).size().isNotZero()
		);
	}

	@DisplayName("회원이 선택한 컬러와 서버의 모든 컬러를 조회한다.")
	@Test
	void findMemberColorTest() {
		/* Given */
		Member member = KIM_JEONG_UK.toMember();
		given(memberRepository.findById(any(Integer.class)))
				.willReturn(Optional.of(member));

		/* When */
		MemberColorsResponse response = memberService.findColors(authMember);

		/* Then */
		assertAll(
				() -> assertThat(response.colorCount()).isEqualTo(Color.values().length),
				() -> assertThat(response.selectColor()).isEqualTo(member.getColor())
		);
	}

	@DisplayName("회원 검색 테스트")
	@Test
	void searchMemberTest() {
		/* Given */
		MemberFixture[] values = MemberFixture.values();
		List<Member> members = new ArrayList<>();
		IntStream.range(0, values.length).forEach(i -> {
			Member member = values[i].toMemberWithId(i);
			member.changeProfileImage("profile_image_url_" + i);
			members.add(member);
		});
		given(memberRepository.findBySearchCond(any(MemberSearchCond.class)))
				.willReturn(members);

		/* When & Then */
		assertThatCode(() -> {
			MemberSearchResponse res = memberService.searchMembers(authMember, new MemberSearchCond("", 10));
			assertThat(res.count()).isEqualTo(values.length);
		}).doesNotThrowAnyException();
	}

	@DisplayName("회원 프로필 정보 조회 테스트")
	@Nested
	class FindProfileTest {
		@DisplayName("회원의 프로필 정보를 조회한다.")
		@Test
		void findProfileSuccessTest() {
			/* Given */
			Member member = KIM_JEONG_UK.toMember();
			given(memberRepository.findById(any(Integer.class)))
					.willReturn(Optional.of(member));

			/* When */
			FindMemberProfileInfoResponse response = memberService.findProfileInfo(authMember, authMember.getId());

			/* Then */
			assertAll(
					() -> assertThat(response.email()).isEqualTo(member.getEmail()),
					() -> assertThat(response.nickname()).isEqualTo(member.getNickname()),
					() -> assertThat(response.mainColor()).isEqualTo(member.getColor()),
					() -> assertThat(response.profileImage()).isEqualTo(member.getProfileImage())
			);
		}

		@DisplayName("프로필 조회 권한이 없으면 예외를 던진다.")
		@Test
		void validateOwnershipTest() {
			/* Given */
			int memberId = authMember.getId() + 1;

			/* When & Then */
			assertThatThrownBy(() -> memberService.findProfileInfo(authMember, memberId))
					.isInstanceOf(MemberException.class)
					.hasMessageContaining(MemberErrorCode.PERMISSION_DENIED.getMessage());
		}
	}
}