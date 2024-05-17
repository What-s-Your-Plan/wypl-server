package com.butter.wypl.schedule.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.ServiceTest;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.exception.ScheduleErrorCode;
import com.butter.wypl.schedule.exception.ScheduleException;
import com.butter.wypl.schedule.fixture.ScheduleFixture;
import com.butter.wypl.schedule.respository.ScheduleRepository;

@ServiceTest
public class ScheduleServiceUtilsTest {

	private final ScheduleRepository scheduleRepository;

	@Autowired
	public ScheduleServiceUtilsTest(ScheduleRepository scheduleRepository) {
		this.scheduleRepository = scheduleRepository;
	}

	@Test
	@DisplayName("일정이 정상적으로 조회되는지 확인")
	void getSchedule() {
		// Given
		Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
		Schedule savedSchedule = scheduleRepository.save(schedule);

		// When
		Schedule findSchedule = ScheduleServiceUtils.findById(scheduleRepository, savedSchedule.getScheduleId());

		// Then
		assertThat(findSchedule).isNotNull();
		assertThat(findSchedule.getScheduleId()).isEqualTo(savedSchedule.getScheduleId());
	}

	@Test
	@DisplayName("일정이 없을 시 에러 던지는지")
	void getScheduleException() {
		// Given
		Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();

		// When
		// Then
		assertThatThrownBy(() -> {
			ScheduleServiceUtils.findById(scheduleRepository, 1);
		}).isInstanceOf(ScheduleException.class)
				.hasMessageContaining(ScheduleErrorCode.NO_SCHEDULE.getMessage());

	}
}
