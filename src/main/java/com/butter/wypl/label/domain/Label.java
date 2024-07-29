package com.butter.wypl.label.domain;

import java.util.List;

import com.butter.wypl.member.domain.Member;
import com.butter.wypl.schedule.domain.ScheduleInfo;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.label.exception.LabelErrorCode;
import com.butter.wypl.label.exception.LabelException;
import com.butter.wypl.schedule.domain.Schedule;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLRestriction("deleted_at is null")
@Table(name = "label_tbl")
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Builder
	public Label(int labelId, String title, Color color, Member member) {
		titleValidation(title);
		this.labelId = labelId;
		this.title = title;
		this.color = color;
		this.member = member;
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