package com.butter.wypl.schedule.domain;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.group.domain.Group;
import com.butter.wypl.label.domain.Label;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.review.domain.Review;
import com.butter.wypl.schedule.data.request.ScheduleUpdateRequest;
import com.butter.wypl.schedule.exception.ScheduleErrorCode;
import com.butter.wypl.schedule.exception.ScheduleException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLRestriction("deleted_at is null")
@Table(name = "schedule_info_tbl")
public class ScheduleInfo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "schedule_id")
	private int scheduleId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private Group group;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "label_id")
	private Label label;

	@Column(nullable = false, length = 50)
	private String title;

	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Category category;

	@Column(name = "start_datetime", nullable = false)
	private LocalDateTime startDateTime;

	@Column(name = "end_datetime", nullable = false)
	private LocalDateTime endDateTime;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "scheduleInfo")
	private Repetition repetition;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scheduleInfo")
	private List<Schedule> schedules;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scheduleInfo")
	private List<Review> reviews;

	@Builder
	public ScheduleInfo(Member member, Group group, Label label, String title, String description, Category category, LocalDateTime startDateTime, LocalDateTime endDateTime, Repetition repetition) {
		titleValidation(title);
		durationValidation(startDateTime, endDateTime);
		repetitionValidation(startDateTime, endDateTime, repetition);
		this.member = member;
		this.group = group;
		this.label = label;
		this.title = title;
		this.description = description;
		this.category = category;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.repetition = repetition;
	}

	public void update(ScheduleUpdateRequest scheduleUpdateRequest) {
		titleValidation(scheduleUpdateRequest.title());
		durationValidation(scheduleUpdateRequest.startDateTime(), scheduleUpdateRequest.endDateTime());
		this.title = scheduleUpdateRequest.title();
		this.description = scheduleUpdateRequest.description();
		this.startDateTime = scheduleUpdateRequest.startDateTime();
		this.endDateTime = scheduleUpdateRequest.endDateTime();
	}

	public void updateLabel(Label label) {
		this.label = label;
	}

	public void updateRepetition(Repetition repetition) {
		repetitionValidation(startDateTime, endDateTime, repetition);
	}

	public ScheduleInfo toRepetitionSchedule(LocalDateTime startDateTimeTime, LocalDateTime endDateTime) {
		return ScheduleInfo.builder()
			.title(title)
			.description(description)
			.startDateTime(startDateTimeTime)
			.endDateTime(endDateTime)
			.category(category)
				.group(group)
			.label(label)
			.repetition(repetition)
			.build();
	}

	private void titleValidation(String title) {
		if (title == null || title.length() > 50 || title.isEmpty()) {
			throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_TITLE);
		}
	}

	private void durationValidation(LocalDateTime startDateTimeTime, LocalDateTime endDateTime) {
		dateValidation(startDateTimeTime, endDateTime);
		if (startDateTimeTime.isAfter(endDateTime) || startDateTimeTime.isEqual(endDateTime)) {
			throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_DURATION);
		}
	}

	private void repetitionValidation(LocalDateTime startDateTimeTime, LocalDateTime endDateTime, Repetition repetition) {
		if (repetition == null) {
			return;
		}

		weekValidation(repetition.getWeek());

		long scheduleDuration = Duration.between(startDateTimeTime, endDateTime).toMinutes();

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

	private void dateValidation(LocalDateTime startDateTimeTime, LocalDateTime endDateTime) {
		if (startDateTimeTime == null || endDateTime == null) {
			throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_DATE);
		}

		if (startDateTimeTime.isBefore(LocalDateTime.of(1970, 1, 1, 0, 0))) {
			throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_DATE);
		}

		if (endDateTime.isAfter(LocalDateTime.of(2050, 12, 31, 11, 59))) {
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
