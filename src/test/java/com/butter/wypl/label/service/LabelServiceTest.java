package com.butter.wypl.label.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.ServiceTest;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.label.data.request.LabelRequest;
import com.butter.wypl.label.data.response.LabelIdResponse;
import com.butter.wypl.label.data.response.LabelListResponse;
import com.butter.wypl.label.data.response.LabelResponse;
import com.butter.wypl.label.domain.Label;
import com.butter.wypl.label.exception.LabelErrorCode;
import com.butter.wypl.label.exception.LabelException;
import com.butter.wypl.label.fixture.LabelFixture;

@ServiceTest
public class LabelServiceTest {

	private final LabelServiceImpl labelService;

	@Autowired
	public LabelServiceTest(LabelServiceImpl labelService) {
		this.labelService = labelService;
	}

	@DisplayName("생성 test")
	@Nested
	class CreateTest {
		@Test
		@DisplayName("정상적인 데이터 입력시 라벨이 정상적으로 생성되는지 확인")
		void createLabel() {
			//given
			Label label = LabelFixture.STUDY_LABEL.toLabel();

			//when
			LabelResponse savedLabel = labelService.createLabel(
				label.getMemberId(),
				new LabelRequest(label.getTitle(), label.getColor())
			);

			//then
			assertThat(savedLabel).isNotNull();
			assertThat(savedLabel.labelId()).isGreaterThan(0);
			assertThat(savedLabel.memberId()).isEqualTo(label.getMemberId());
			assertThat(savedLabel.title()).isEqualTo(label.getTitle());
			assertThat(savedLabel.color()).isEqualTo(label.getColor());
		}

		@Test
		@DisplayName("생성시 제목 입력이 없을시 오류 던지는지 확인")
		void createLabelTitleException() {
			//given
			Label label = LabelFixture.STUDY_LABEL.toLabel();

			//when
			//then
			assertThatThrownBy(() -> {
				labelService.createLabel(
					label.getMemberId(),
					new LabelRequest(null, label.getColor())
				);
			}).isInstanceOf(LabelException.class)
				.hasMessageContaining(LabelErrorCode.NOT_APPROPRIATE_TITLE.getMessage());
		}

	}

	@DisplayName("수정 test")
	@Nested
	class UpdateTest {
		LabelResponse savedLabel;
		Label label;

		@BeforeEach
		void init() {
			label = LabelFixture.STUDY_LABEL.toLabel();
			savedLabel = labelService.createLabel(
				label.getMemberId(),
				new LabelRequest(label.getTitle(), label.getColor())
			);
		}

		@Test
		@DisplayName("정상적인 입력시 수정이 되는지 확인")
		void updateLabel() {
			//given
			//when
			LabelResponse updatedLabel = labelService.updateLabel(
				label.getMemberId(),
				savedLabel.labelId(),
				new LabelRequest("바뀐 제목", Color.labelBlue));

			//then
			assertThat(updatedLabel.labelId()).isEqualTo(savedLabel.labelId());
			assertThat(updatedLabel.memberId()).isEqualTo(label.getMemberId());
			assertThat(updatedLabel.title()).isEqualTo("바뀐 제목");
			assertThat(updatedLabel.color()).isEqualTo(Color.labelBlue);
		}

		@Test
		@DisplayName("수정 시 제목 입력이 없을시 오류 던지는지 확인")
		void updateLabelTitleException() {
			//given
			//when
			//then
			assertThatThrownBy(() -> {
				labelService.updateLabel(label.getMemberId(), savedLabel.labelId(),
					new LabelRequest(null, Color.labelBlue));
			}).isInstanceOf(LabelException.class)
				.hasMessageContaining(LabelErrorCode.NOT_APPROPRIATE_TITLE.getMessage());
		}

		@Test
		@DisplayName("멤버 id와 라벨 id가 일치하지 않을때 에러")
		void checkValidation() {
			//given
			//when
			//then
			assertThatThrownBy(() -> {
				labelService.updateLabel(
					2,
					savedLabel.labelId(),
					new LabelRequest("운동가기", Color.labelBlue)
				);
			}).isInstanceOf(LabelException.class)
				.hasMessageContaining(LabelErrorCode.NO_PERMISSION_UPDATE.getMessage());
		}
	}

	@DisplayName("삭제 test")
	@Nested
	class DeleteTest {
		@Test
		@DisplayName("라벨 삭제가 정상 동작하는지")
		void deleteLabel() {
			//given
			Label label = LabelFixture.STUDY_LABEL.toLabel();
			LabelResponse savedLabel = labelService.createLabel(
				label.getMemberId(),
				new LabelRequest(label.getTitle(), label.getColor())
			);

			//then
			LabelIdResponse deletedLabelId = labelService.deleteLabel(savedLabel.labelId(), savedLabel.memberId());

			//when
			assertThat(savedLabel.labelId()).isEqualTo(deletedLabelId.labelId());
		}
	}

	@DisplayName("라벨 id로 라벨 조회")
	@Nested
	class GetLabelByLabelId {
		@Test
		@DisplayName("라벨 id 라벨 조회가 정상적으로 동작하는지")
		void getLabel() {
			//given
			Label label = LabelFixture.STUDY_LABEL.toLabel();
			LabelResponse savedLabel = labelService.createLabel(
				label.getMemberId(),
				new LabelRequest(label.getTitle(), label.getColor())
			);

			//then
			//when
			assertThatCode(() -> {
				labelService.getLabelByLabelId(savedLabel.labelId());
			}).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("라벨 id 라벨 조회 시 라벨이 없는 경우 에러를 던지는지")
		void getLabelExistException() {
			//given
			//then
			//when
			assertThatThrownBy(() -> {
				labelService.getLabelByLabelId(0);
			}).isInstanceOf(LabelException.class)
				.hasMessageContaining(LabelErrorCode.NOT_FOUND.getMessage());
		}
	}

	@DisplayName("멤버 id로 라벨 리스트 조회")
	@Nested
	class GetLabelsByMemberId {

		@Test
		@DisplayName("정상 작동 확인")
		void getLabelByMemberId() {
			//given
			Label label1 = LabelFixture.STUDY_LABEL.toLabel();
			labelService.createLabel(label1.getMemberId(),
				new LabelRequest(label1.getTitle(), label1.getColor()));

			Label label2 = LabelFixture.STUDY_LABEL.toLabel();
			labelService.createLabel(label2.getMemberId(),
				new LabelRequest(label2.getTitle(), label2.getColor()));

			//when
			LabelListResponse result = labelService.getLabelsByMemberId(label1.getMemberId());

			//then
			assertThat(result.labelCount()).isEqualTo(2);
		}
	}

}
