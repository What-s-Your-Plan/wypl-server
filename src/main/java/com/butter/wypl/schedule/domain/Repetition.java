package com.butter.wypl.schedule.domain;

import java.time.LocalDate;

import org.hibernate.annotations.SQLRestriction;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.schedule.exception.ScheduleErrorCode;
import com.butter.wypl.schedule.exception.ScheduleException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLRestriction("deleted_at is null")
public class Repetition extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "repetition_id")
	private int repetitionId;

	@Column(name = "repetition_cycle")
	@Enumerated(EnumType.STRING)
	private RepetitionCycle repetitionCycle;

	@Column(name = "repetition_start_date")
	private LocalDate repetitionStartDate;

	@Column(name = "repetition_end_date")
	private LocalDate repetitionEndDate;

	private Integer week;

	@Column(name = "day_of_week")
	private int dayOfWeek;

	@Builder
	public Repetition(int repetitionId, RepetitionCycle repetitionCycle, LocalDate repetitionStartDate,
		LocalDate repetitionEndDate, Integer week, int dayOfWeek) {
		dayOfWeekValidation(dayOfWeek);
		durationValidation(repetitionStartDate, repetitionEndDate);

		this.repetitionId = repetitionId;
		this.repetitionCycle = repetitionCycle;
		this.repetitionStartDate = repetitionStartDate;
		this.repetitionEndDate = repetitionEndDate;
		this.week = week;
		this.dayOfWeek = dayOfWeek;
	}

	private void dayOfWeekValidation(int dayOfWeek) {
		if (dayOfWeek > 127) {
			throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_DAY_OF_WEEK);
		}
	}

	private void durationValidation(LocalDate repetitionStartDate, LocalDate repetitionEndDate) {
		if (repetitionEndDate != null && repetitionStartDate.isAfter(repetitionEndDate)) {
			throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_REPETITION_DURATION);
		}
	}

}
