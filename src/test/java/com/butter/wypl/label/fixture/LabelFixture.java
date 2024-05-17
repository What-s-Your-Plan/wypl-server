package com.butter.wypl.label.fixture;

import com.butter.wypl.global.common.Color;
import com.butter.wypl.label.domain.Label;

public enum LabelFixture {
	EXERCISE_LABEL("운동", Color.labelCharcoal, 1),
	STUDY_LABEL("알고리즘 스터디", Color.labelCharcoal, 1),
	;

	private final String title;

	private final Color color;

	private final int memberId;

	LabelFixture(String title, Color color, int memberId) {
		this.title = title;
		this.color = color;
		this.memberId = memberId;
	}

	public Label toLabel() {
		return Label.builder()
			.title(title)
			.color(color)
			.memberId(memberId)
			.build();
	}
}
