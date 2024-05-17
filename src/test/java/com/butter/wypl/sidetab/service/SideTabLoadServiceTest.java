package com.butter.wypl.sidetab.service;

import static com.butter.wypl.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
import com.butter.wypl.sidetab.data.response.DDayWidgetResponse;
import com.butter.wypl.sidetab.data.response.GoalWidgetResponse;
import com.butter.wypl.sidetab.data.response.MemoWidgetResponse;
import com.butter.wypl.sidetab.domain.SideTab;
import com.butter.wypl.sidetab.domain.embedded.DDayWidget;
import com.butter.wypl.sidetab.domain.embedded.MemoWidget;
import com.butter.wypl.sidetab.repository.SideTabRepository;

@MockServiceTest
class SideTabLoadServiceTest {

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

	@DisplayName("목표를 조회한다.")
	@Test
	void findGoalWidgetTest() {
		/* Given */
		Member member = KIM_JEONG_UK.toMember();
		given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(member));

		SideTab sideTab = SideTab.from(member);
		given(sideTabRepository.findById(anyInt()))
				.willReturn(Optional.of(sideTab));

		/* When */
		GoalWidgetResponse response = sideTabService.findGoal(authMember, 0);

		/* Then */
		assertThat(response).isNotNull();
	}

	@DisplayName("디데이를 조회한다.")
	@Test
	void findDDayWidgetTest() {
		/* Given */
		Member member = KIM_JEONG_UK.toMember();
		given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(member));

		SideTab sideTab = SideTab.from(member);
		sideTab.updateDDay(DDayWidget.of("디데이", LocalDate.now()));
		given(sideTabRepository.findById(anyInt()))
				.willReturn(Optional.of(sideTab));

		/* When */
		DDayWidgetResponse response = sideTabService.findDDay(authMember, 0);

		/* Then */
		assertThat(response).isNotNull();
	}

	@DisplayName("메모를 조회한다.")
	@Test
	void findMemoWidgetTest() {
		/* Given */
		Member member = KIM_JEONG_UK.toMember();
		given(memberRepository.findById(anyInt()))
				.willReturn(Optional.of(member));

		SideTab sideTab = SideTab.from(member);
		sideTab.updateMemo(MemoWidget.from("메모"));
		given(sideTabRepository.findById(anyInt()))
				.willReturn(Optional.of(sideTab));

		/* When */
		MemoWidgetResponse response = sideTabService.findMemo(authMember, 0);

		/* Then */
		assertThat(response).isNotNull();
	}
}