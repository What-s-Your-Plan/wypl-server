package com.butter.wypl.label.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.butter.wypl.global.common.Color;
import com.butter.wypl.label.fixture.LabelFixture;

public class LabelTest {

	@Test
	@DisplayName("update 메소드가 정상 작동하는지 확인")
	void update() {
		//given
		Label label = LabelFixture.STUDY_LABEL.toLabel();

		//when
		label.update("제목 바뀜", Color.labelBrown);

		//then
		assertThat(label.getMemberId()).isEqualTo(LabelFixture.STUDY_LABEL.toLabel().getMemberId());
		assertThat(label.getTitle()).isEqualTo("제목 바뀜");
		assertThat(label.getColor()).isEqualTo(Color.labelBrown);
	}

}
