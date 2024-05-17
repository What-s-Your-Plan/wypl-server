package com.butter.wypl.sidetab.domain;

import static com.butter.wypl.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.butter.wypl.member.domain.Member;
import com.butter.wypl.sidetab.domain.embedded.GoalWidget;

class SideTabTest {

	@DisplayName("SideTab 생성에 성공한다.")
	@Test
	void generateSideTab() {
		/* Given */
		Member member = KIM_JEONG_UK.toMember();

		/* When */
		SideTab sideTab = SideTab.from(member);

		/* Then */
		assertAll(
			() -> assertThat(sideTab.getGoal()).isNotNull(),
			() -> assertThat(sideTab.getMemo()).isNotNull(),
			() -> assertThat(sideTab.getDDayDate()).isNotNull(),
			() -> assertThat(sideTab.getDDayTitle()).isNotNull()
		);
	}

	@DisplayName("SideTab 수정 테스트")
	@Nested
	class SideTabUpdateTest {

		private SideTab sideTab;

		@BeforeEach
		void setUp() {
			Member member = KIM_JEONG_UK.toMember();
			sideTab = SideTab.from(member);
		}

		@DisplayName("목표를 수정한다.")
		@Test
		void goalUpdateTest() {
			/* Given */
			String goalAsString = "새로운 목표";
			GoalWidget newGoalWidget = GoalWidget.from(goalAsString);

			/* When */
			sideTab.updateGoal(newGoalWidget);

			/* Then */
			assertThat(sideTab.getGoal()).isEqualTo(goalAsString);
		}
	}
}