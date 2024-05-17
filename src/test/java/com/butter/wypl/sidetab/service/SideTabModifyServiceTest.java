package com.butter.wypl.sidetab.service;

import static com.butter.wypl.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.sidetab.data.request.DDayUpdateRequest;
import com.butter.wypl.sidetab.data.request.GoalUpdateRequest;
import com.butter.wypl.sidetab.data.request.MemoUpdateRequest;
import com.butter.wypl.sidetab.data.response.DDayWidgetResponse;
import com.butter.wypl.sidetab.data.response.GoalWidgetResponse;
import com.butter.wypl.sidetab.data.response.MemoWidgetResponse;
import com.butter.wypl.sidetab.domain.SideTab;
import com.butter.wypl.sidetab.repository.SideTabRepository;

@MockServiceTest
class SideTabModifyServiceTest {

	@InjectMocks
	private SideTabServiceImpl sideTabService;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private SideTabRepository sideTabRepository;

	private AuthMember authMember;

	@BeforeEach
	void setUp() {
		authMember = AuthMember.from(0);
	}

	@DisplayName("목표를 수정한다.")
	@Test
	void goalUpdateSuccessTest() {
		/* Given */
		String goalAsString = "새로운 목표";
		GoalUpdateRequest request = new GoalUpdateRequest(goalAsString);

		Member member = KIM_JEONG_UK.toMember();
		given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(member));

		SideTab sideTab = SideTab.from(member);
		given(sideTabRepository.findById(anyInt()))
				.willReturn(Optional.of(sideTab));

		/* When */
		GoalWidgetResponse response = sideTabService.updateGoal(authMember, 0, request);

		/* Then */
		assertThat(response.content()).isEqualTo(goalAsString);
	}

	@DisplayName("디데이를 수정한다.")
	@Test
	void dDayUpdateSuccessTest() {
		/* Given */
		DDayUpdateRequest request = new DDayUpdateRequest("디데이", LocalDate.now());

		Member member = KIM_JEONG_UK.toMember();
		given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(member));

		SideTab sideTab = SideTab.from(member);
		given(sideTabRepository.findById(anyInt()))
				.willReturn(Optional.of(sideTab));

		/* When */
		DDayWidgetResponse response = sideTabService.updateDDay(authMember, 0, request);

		/* Then */
		assertAll(
				() -> assertThat(response.title()).isEqualTo(request.title()),
				() -> assertThat(response.dDay()).isEqualTo("D-DAY")
		);
	}

	@DisplayName("메모를 수정한다.")
	@Test
	void memoUpdateSuccessTest() {
		/* Given */
		String memoAsString = "새로운 메모";
		MemoUpdateRequest request = new MemoUpdateRequest(memoAsString);

		Member member = KIM_JEONG_UK.toMember();
		given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(member));

		SideTab sideTab = SideTab.from(member);
		given(sideTabRepository.findById(anyInt()))
				.willReturn(Optional.of(sideTab));

		/* When */
		MemoWidgetResponse response = sideTabService.updateMemo(authMember, 0, request);

		/* Then */
		assertThat(response.memo()).isEqualTo(memoAsString);
	}
}