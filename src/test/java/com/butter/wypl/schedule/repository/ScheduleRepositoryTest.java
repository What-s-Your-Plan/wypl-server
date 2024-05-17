package com.butter.wypl.schedule.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.JpaRepositoryTest;
import com.butter.wypl.schedule.domain.Repetition;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.fixture.ScheduleFixture;
import com.butter.wypl.schedule.fixture.embedded.RepetitionFixture;
import com.butter.wypl.schedule.respository.RepetitionRepository;
import com.butter.wypl.schedule.respository.ScheduleRepository;

@JpaRepositoryTest
public class ScheduleRepositoryTest {

	private final ScheduleRepository scheduleRepository;
	private final RepetitionRepository repetitionRepository;

	@Autowired
	public ScheduleRepositoryTest(ScheduleRepository scheduleRepository, RepetitionRepository repetitionRepository) {
		this.scheduleRepository = scheduleRepository;
		this.repetitionRepository = repetitionRepository;
	}

	@Test
	@DisplayName("일정이 정상적으로 등록된다")
	void createSchedule() {
		//given
		Schedule schedule = ScheduleFixture.LABEL_REPEAT_PERSONAL_SCHEDULE.toSchedule();

		//when
		Schedule savedSchedule = scheduleRepository.save(schedule);

		//then
		assertThat(savedSchedule).isNotNull();
		assertThat(savedSchedule.getGroupId()).isNull();
	}

	@Test
	@DisplayName("일정이 정상적으로 조회된다")
	void getSchedule() {
		//given
		Schedule schedule = ScheduleFixture.REPEAT_PERSONAL_SCHEDULE.toSchedule();
		Schedule savedSchedule = scheduleRepository.save(schedule);

		//when
		Optional<Schedule> getSchedule = scheduleRepository.findById(savedSchedule.getScheduleId());

		//then
		assertThat(getSchedule).isNotNull();
		if (getSchedule.isPresent()) {
			assertThat(getSchedule.get().getScheduleId()).isEqualTo(savedSchedule.getScheduleId());
			assertThat(getSchedule.get().getRepetition()).isEqualTo(savedSchedule.getRepetition());
		}
	}

	@Test
	@DisplayName("반복의 이전 일정이 모두 조회된다.")
	void getScheduleByRepetition() {
		//given
		Repetition repetition = RepetitionFixture.MONTHLY_REPETITION.toRepetition();
		repetitionRepository.save(repetition);

		Schedule schedule1 = scheduleRepository.save(ScheduleFixture.REPEAT_PERSONAL_SCHEDULE.toSchedule());
		schedule1.updateRepetition(repetition);

		Schedule schedule2 = scheduleRepository.save(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule());
		schedule2.updateRepetition(repetition);

		//when
		List<Schedule> scheduleList = scheduleRepository.findAllByRepetitionAndStartDateBefore(repetition,
				schedule2.getStartDate());

		//then
		assertThat(scheduleList.size()).isEqualTo(1);
	}

	@Test
	@DisplayName("반복이 같은 이후 일정만 조회")
	void getScheduleByRepetitionAndStartDate() {
		// Given
		Repetition repetition = RepetitionFixture.MONTHLY_REPETITION.toRepetition();
		repetitionRepository.save(repetition);

		Schedule schedule1 = scheduleRepository.save(ScheduleFixture.REPEAT_PERSONAL_SCHEDULE.toSchedule());
		schedule1.updateRepetition(repetition);

		Schedule schedule2 = scheduleRepository.save(ScheduleFixture.PERSONAL_SCHEDULE.toSchedule());
		schedule2.updateRepetition(repetition);

		// When
		List<Schedule> scheduleList = scheduleRepository.findAllByRepetitionAndStartDateAfter(repetition,
				schedule1.getStartDate());

		// Then
		assertThat(scheduleList.size()).isEqualTo(1);
	}
}
