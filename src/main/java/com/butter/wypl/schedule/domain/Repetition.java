package com.butter.wypl.schedule.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.schedule.exception.ScheduleErrorCode;
import com.butter.wypl.schedule.exception.ScheduleException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLRestriction("deleted_at is null")
@Table(name = "repetition_tbl")
public class Repetition extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "repetition_id")
	private int repetitionId;

	@OneToOne
	@JoinColumn(name = "schedule_info_id")
	private ScheduleInfo scheduleInfo;

	@Column(name = "repetition_cycle")
	@Enumerated(EnumType.STRING)
	private RepetitionCycle repetitionCycle;

	@Column(name = "day_of_week")
	private int dayOfWeek;

	@Column(name = "repetition_start_date")
	private LocalDate repetitionStartDate;

	@Column(name = "repetition_end_date")
	private LocalDate repetitionEndDate;

	private Integer week;

	@OneToMany(mappedBy = "repetition")
	private List<RepetitionUpdateHistory> repetitionUpdateHistoryList;

	@Builder
	public Repetition(ScheduleInfo scheduleInfo, RepetitionCycle repetitionCycle, int dayOfWeek, LocalDate repetitionStartDate, LocalDate repetitionEndDate, Integer week) {
		dayOfWeekValidation(dayOfWeek);
		durationValidation(repetitionStartDate, repetitionEndDate);
		this.scheduleInfo = scheduleInfo;
		this.repetitionCycle = repetitionCycle;
		this.dayOfWeek = dayOfWeek;
		this.repetitionStartDate = repetitionStartDate;
		this.repetitionEndDate = repetitionEndDate;
		this.week = week;
		this.repetitionUpdateHistoryList = new ArrayList<>();
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
