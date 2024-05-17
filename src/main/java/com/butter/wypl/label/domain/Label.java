package com.butter.wypl.label.domain;

import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.label.exception.LabelErrorCode;
import com.butter.wypl.label.exception.LabelException;
import com.butter.wypl.schedule.domain.Schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLRestriction("deleted_at is null")
public class Label extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "label_id")
	private int labelId;

	@Column(nullable = false, length = 15)
	private String title;

	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private Color color;

	@Column(name = "member_id", nullable = false)
	private int memberId;

	@OneToMany(mappedBy = "label")
	private List<Schedule> schedules;

	@Builder
	public Label(int labelId, String title, Color color, int memberId, List<Schedule> schedules) {
		titleValidation(title);

		this.labelId = labelId;
		this.title = title;
		this.color = color;
		this.memberId = memberId;
		this.schedules = schedules;
	}

	public void update(String title, Color color) {
		titleValidation(title);

		this.title = title;
		this.color = color;
	}

	private void titleValidation(String title) {
		if (title == null || title.length() > 15) {
			throw new LabelException(LabelErrorCode.NOT_APPROPRIATE_TITLE);
		}
	}
}