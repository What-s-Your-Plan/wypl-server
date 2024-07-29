package com.butter.wypl.schedule.domain;

import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.label.domain.Label;
import com.butter.wypl.schedule.data.request.ScheduleUpdateRequest;
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
@Table(name = "schedule_tbl")
public class Schedule extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "schedule_id")
	private int scheduleId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_info_id")
	private ScheduleInfo scheduleInfo;

	@Column(name = "start_datetime", nullable = false)
	private LocalDateTime startDateTime;

	@Column(name = "end_datetime", nullable = false)
	private LocalDateTime endDateTime;

	@Builder
	public Schedule(ScheduleInfo scheduleInfo, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		this.scheduleInfo = scheduleInfo;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
	}

	private void titleValidation(String title) {
		if (title == null || title.length() > 50 || title.isEmpty()) {
			throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_TITLE);
		}
	}

	private void durationValidation(LocalDateTime startDate, LocalDateTime endDate) {
		dateValidation(startDate, endDate);
		if (startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
			throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_DURATION);
		}
	}

	private void repetitionValidation(LocalDateTime startDate, LocalDateTime endDate, Repetition repetition) {
		if (repetition == null) {
			return;
		}

		weekValidation(repetition.getWeek());

		long scheduleDuration = Duration.between(startDate, endDate).toMinutes();

		long repetitionDuration = switch (repetition.getRepetitionCycle()) {
			case WEEK -> {
				yield 7 * 24 * 60;
			}
			case MONTH -> {
				yield 31 * 24 * 60;
			}
			case YEAR -> {
				yield 365 * 24 * 60;
			}
		};

		if (scheduleDuration > repetitionDuration) {
			throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_REPETITION_DURATION);
		}
	}

	private void dateValidation(LocalDateTime startDate, LocalDateTime endDate) {
		if (startDate == null || endDate == null) {
			throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_DATE);
		}

		if (startDate.isBefore(LocalDateTime.of(1970, 1, 1, 0, 0))) {
			throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_DATE);
		}

		if (endDate.isAfter(LocalDateTime.of(2050, 12, 31, 11, 59))) {
			throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_DATE);
		}
	}

	private void weekValidation(Integer week) {
		if (week == null) {
			return;
		}

		if (week <= 0 || week > 100) {
			throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_WEEK);
		}
	}
}
