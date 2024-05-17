package com.butter.wypl.schedule.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.JpaRepositoryTest;
import com.butter.wypl.schedule.domain.Repetition;
import com.butter.wypl.schedule.fixture.embedded.RepetitionFixture;
import com.butter.wypl.schedule.respository.RepetitionRepository;

@JpaRepositoryTest
public class RepetitionRepositoryTest {

	private final RepetitionRepository repetitionRepository;

	@Autowired
	public RepetitionRepositoryTest(RepetitionRepository repetitionRepository) {
		this.repetitionRepository = repetitionRepository;
	}

	@Test
	@DisplayName("저장되는지 조회")
	void saveRepetition() {
		// Given
		Repetition repetition = RepetitionFixture.MONTHLY_REPETITION.toRepetition();

		// When
		Repetition savedRepetition = repetitionRepository.save(repetition);

		// Then
		assertThat(savedRepetition).isNotNull();
	}

	@Test
	@DisplayName("반복 조회")
	void getRepetition() {
		// Given
		Repetition repetition = RepetitionFixture.MONTHLY_REPETITION.toRepetition();
		Repetition savedRepetition = repetitionRepository.save(repetition);

		// When
		Optional<Repetition> findRepetition = repetitionRepository.findById(savedRepetition.getRepetitionId());

		// Then
		assertThat(findRepetition).isNotNull();
		if (findRepetition.isPresent()) {
			assertThat(findRepetition.get().getRepetitionId()).isEqualTo(savedRepetition.getRepetitionId());
			assertThat(findRepetition.get().getRepetitionCycle()).isEqualTo(savedRepetition.getRepetitionCycle());
			assertThat(findRepetition.get().getRepetitionStartDate()).isEqualTo(
				savedRepetition.getRepetitionStartDate());
		}
	}
}
