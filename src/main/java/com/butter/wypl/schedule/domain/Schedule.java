package com.butter.wypl.schedule.domain;

import java.time.Duration;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.label.domain.Label;
import com.butter.wypl.schedule.data.request.ScheduleUpdateRequest;
import com.butter.wypl.schedule.exception.ScheduleErrorCode;
import com.butter.wypl.schedule.exception.ScheduleException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLRestriction("deleted_at is null")
public class Schedule extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "schedule_id")
	private int scheduleId;

	@Column(nullable = false, length = 50)
	private String title;

	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Category category;

	@Column(name = "group_id")
	private Integer groupId;

	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDateTime endDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "label_id")
	private Label label;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "repetition_id")
	private Repetition repetition;

	@Builder
	public Schedule(int scheduleId, String title, String description, Category category, Integer groupId,
		LocalDateTime startDate, LocalDateTime endDate, Label label, Repetition repetition) {
		titleValidation(title);
		durationValidation(startDate, endDate);
		repetitionValidation(startDate, endDate, repetition);

		this.scheduleId = scheduleId;
		this.title = title;
		this.description = description;
		this.category = category;
		this.groupId = groupId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.label = label;
		this.repetition = repetition;
	}

	public void update(ScheduleUpdateRequest scheduleUpdateRequest) {
		titleValidation(scheduleUpdateRequest.title());
		durationValidation(scheduleUpdateRequest.startDate(), scheduleUpdateRequest.endDate());

		this.title = scheduleUpdateRequest.title();
		this.description = scheduleUpdateRequest.description();
		this.startDate = scheduleUpdateRequest.startDate();
		this.endDate = scheduleUpdateRequest.endDate();
	}

	public void updateLabel(Label label) {
		this.label = label;
	}

	public void updateRepetition(Repetition repetition) {
		repetitionValidation(startDate, endDate, repetition);

		this.repetition = repetition;
	}

	public Schedule toRepetitionSchedule(LocalDateTime startDate, LocalDateTime endDate) {
		return Schedule.builder()
			.title(title)
			.description(description)
			.startDate(startDate)
			.endDate(endDate)
			.category(category)
			.groupId(groupId)
			.label(label)
			.repetition(repetition)
			.build();
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
