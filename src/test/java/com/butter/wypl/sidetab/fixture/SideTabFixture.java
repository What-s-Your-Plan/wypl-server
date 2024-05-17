package com.butter.wypl.sidetab.fixture;

import java.time.LocalDate;

public enum SideTabFixture {
	SIDE_TAB_NULL(null, null, null, null),
	SIDE_TAB_ONE("메모를 작성합니다.", "목표는 다음과 같습니다.", LocalDate.of(2024, 4, 23), "프로젝트 시작"),
	;

	private final String memo;
	private final String goal;
	private final String title;
	private final LocalDate dDay;

	SideTabFixture(String memo, String goal, LocalDate dDay, String title) {
		this.memo = memo;
		this.goal = goal;
		this.dDay = dDay;
		this.title = title;
	}

	public LocalDate getDDay() {
		return dDay;
	}

	public String getGoal() {
		return goal;
	}

	public String getMemo() {
		return memo;
	}

	public String getTitle() {
		return title;
	}
}
