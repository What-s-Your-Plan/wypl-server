package com.butter.wypl.schedule.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.ServiceTest;
import com.butter.wypl.schedule.domain.Repetition;
import com.butter.wypl.schedule.fixture.embedded.RepetitionFixture;

@ServiceTest
public class RepetitionServiceTest {

	private final RepetitionService repetitionService;

	@Autowired
	public RepetitionServiceTest(RepetitionService repetitionService) {
		this.repetitionService = repetitionService;
	}

	@Test
	@DisplayName("repetition 생성")
	void createRepetition() {
		// Given
		Repetition repetition = RepetitionFixture.MONTHLY_REPETITION.toRepetition();

		// When
		Repetition savedRepetition = repetitionService.createRepetition(repetition);

		// Then
		assertThat(savedRepetition).isNotNull();
		assertThat(savedRepetition.getRepetitionCycle()).isEqualTo(repetition.getRepetitionCycle());
		assertThat(savedRepetition.getRepetitionStartDate()).isEqualTo(repetition.getRepetitionStartDate());
		assertThat(savedRepetition.getRepetitionEndDate()).isEqualTo(repetition.getRepetitionEndDate());
	}

	@Test
	@DisplayName("반복 삭제")
	void deleteRepetition() {
		// Given
		Repetition repetition = RepetitionFixture.MONTHLY_REPETITION.toRepetition();
		Repetition savedRepetition = repetitionService.createRepetition(repetition);

		// When
		repetitionService.deleteRepetition(savedRepetition);

		// Then
		assertThat(savedRepetition.getDeletedAt()).isNotNull();
	}
}